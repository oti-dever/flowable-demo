package cn.cutepikachu.flowable.service.impl;

import cn.cutepikachu.flowable.constant.FlowableConstant;
import cn.cutepikachu.flowable.dao.EmployeeBaseInfoDAO;
import cn.cutepikachu.flowable.dao.ExpenseReimbursementDAO;
import cn.cutepikachu.flowable.dao.ProcessDAO;
import cn.cutepikachu.flowable.enums.ExpenseReimbursementApprovalType;
import cn.cutepikachu.flowable.enums.ExpenseReimbursementStatus;
import cn.cutepikachu.flowable.enums.error.ProcessErrorEnum;
import cn.cutepikachu.flowable.exception.BusinessException;
import cn.cutepikachu.flowable.flowable.IFlowableService;
import cn.cutepikachu.flowable.flowable.event.impl.ProcessStartedEvent;
import cn.cutepikachu.flowable.flowable.event.impl.TaskApprovedEvent;
import cn.cutepikachu.flowable.flowable.service.AbstractProcessService;
import cn.cutepikachu.flowable.model.dto.ExpenseReimbursementApprovalDTO;
import cn.cutepikachu.flowable.model.dto.ExpenseReimbursementCreateDTO;
import cn.cutepikachu.flowable.model.dto.ExpenseReimbursementUpdateDTO;
import cn.cutepikachu.flowable.model.entity.EmployeeBaseInfo;
import cn.cutepikachu.flowable.model.entity.ExpenseReimbursement;
import cn.cutepikachu.flowable.model.entity.Process;
import cn.cutepikachu.flowable.model.vo.ExpenseReimbursementVO;
import cn.cutepikachu.flowable.service.ExpenseReimbursementService;
import cn.cutepikachu.flowable.util.EqualUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cn.cutepikachu.flowable.constant.FlowableConstant.EXPIRE_DURATION;
import static cn.cutepikachu.flowable.constant.FlowableConstant.ExpenseReimbursement.*;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 报销申请服务实现
 * @since 2025/8/28 12:00:00
 */
@Service
public class ExpenseReimbursementServiceImpl
        extends AbstractProcessService
        implements ExpenseReimbursementService {

    @Getter
    @Resource
    private ProcessDAO processDAO;

    @Getter
    @Resource
    private IFlowableService flowableService;

    @Getter
    @Resource
    private EmployeeBaseInfoDAO employeeBaseInfoDAO;

    @Resource
    private ExpenseReimbursementDAO expenseReimbursementDAO;

    @Override
    public <T> ProcessStartedEvent startProcessBusiness(Long processId, T startDTO) {
        ExpenseReimbursementCreateDTO dto;
        try {
            dto = (ExpenseReimbursementCreateDTO) startDTO;
        } catch (ClassCastException e) {
            throw new BusinessException("参数类型错误");
        }
        validateCreate(dto);
        LocalDateTime expenseDate = LocalDateTimeUtil.parse(dto.getExpenseDate());

        ExpenseReimbursement entity = new ExpenseReimbursement();
        entity.setApplicantEmpCode(dto.getApplicantEmpCode());
        entity.setExpenseType(dto.getExpenseType());
        entity.setAmount(dto.getAmount());
        entity.setReason(dto.getReason());
        entity.setExpenseDate(expenseDate);
        entity.setAttachments(dto.getAttachments());
        entity.setCcEmpCodes(dto.getCcEmpCodes());
        entity.setStatus(ExpenseReimbursementStatus.PENDING_DEPT_APPROVAL.getCode());

        boolean saved = expenseReimbursementDAO.save(entity);
        if (!saved) {
            throw new BusinessException("报销申请保存失败");
        }
        Long businessId = entity.getId();

        // 构建流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(FlowableConstant.ASSIGNEE_STRATEGY, ASSIGNEE_STRATEGY_MAP);
        variables.put(AMOUNT, entity.getAmount());
        String expireDuration = Duration.ofDays(7).toString();
        variables.put(EXPIRE_DURATION, expireDuration);

        return new ProcessStartedEvent(
                PROC_DEF_KEY,
                businessId,
                variables
        );
    }

    @Override
    public Boolean cancelProcessBusiness(Long businessId, String comment) {
        ExpenseReimbursement er = expenseReimbursementDAO.getById(businessId);
        if (er == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }
        // TODO 用户上下文获取申请人工号
        // if (!Objects.equals(er.getApplicantEmpCode(), dto.getApplicantEmpCode())) {
        //     throw new BusinessException(ProcessErrorEnum.NO_PERMISSION);
        // }
        if (EqualUtil.equalsAny(er.getStatus(),
                ExpenseReimbursementStatus.COMPLETED.getCode(),
                ExpenseReimbursementStatus.CANCELED.getCode(),
                ExpenseReimbursementStatus.DISCARDED.getCode(),
                ExpenseReimbursementStatus.EXPIRED.getCode())
        ) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_CANNOT_CANCELLED);
        }

        expenseReimbursementDAO.updateStatusById(businessId, ExpenseReimbursementStatus.CANCELED.getCode());
        return Boolean.TRUE;
    }

    @Override
    public Boolean discardProcessBusiness(Long businessId, String comment) {
        ExpenseReimbursement er = expenseReimbursementDAO.getById(businessId);
        if (er == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }
        if (EqualUtil.equalsAny(er.getStatus(),
                ExpenseReimbursementStatus.COMPLETED.getCode(),
                ExpenseReimbursementStatus.CANCELED.getCode(),
                ExpenseReimbursementStatus.DISCARDED.getCode(),
                ExpenseReimbursementStatus.EXPIRED.getCode())) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_CANNOT_DISCARD);
        }

        expenseReimbursementDAO.updateStatusById(businessId, ExpenseReimbursementStatus.DISCARDED.getCode());
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExpenseReimbursementVO update(ExpenseReimbursementUpdateDTO dto) {
        validateUpdate(dto);

        ExpenseReimbursement er = expenseReimbursementDAO.getById(dto.getId());
        if (er == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }

        if (!Objects.equals(er.getStatus(), ExpenseReimbursementStatus.RE_SUBMIT.getCode())) {
            throw new BusinessException("只有草稿状态的报销申请才能更新");
        }

        LocalDateTime expenseDate = LocalDateTimeUtil.parse(dto.getExpenseDate());

        // 更新报销申请信息
        er.setExpenseType(dto.getExpenseType());
        er.setAmount(dto.getAmount());
        er.setReason(dto.getReason());
        er.setExpenseDate(expenseDate);
        er.setAttachments(dto.getAttachments());
        er.setCcEmpCodes(dto.getCcEmpCodes());

        boolean updated = expenseReimbursementDAO.updateById(er);
        if (!updated) {
            throw new BusinessException("报销申请更新失败");
        }

        // 查找当前的任务
        Process process = processDAO.getByBusinessId(er.getId());
        Task task = flowableService.getCurrentTask(process.getProcessInstanceId());
        if (task == null) {
            throw new BusinessException(ProcessErrorEnum.TASK_NOT_FOUND);
        }
        // 更新流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(AMOUNT, er.getAmount());

        // 完成填写报销单任务，让流程继续
        flowableService.completeTask(task.getId(), variables);

        // 更新状态为待审批
        expenseReimbursementDAO.updateStatusById(er.getId(), ExpenseReimbursementStatus.PENDING_DEPT_APPROVAL.getCode());

        return toVO(er);
    }

    @Override
    public <T> TaskApprovedEvent approveTaskBusiness(Long businessId, T approveDTO) {
        ExpenseReimbursementApprovalDTO dto;
        try {
            dto = (ExpenseReimbursementApprovalDTO) approveDTO;
        } catch (ClassCastException e) {
            throw new BusinessException("参数类型错误");
        }
        ExpenseReimbursement er = expenseReimbursementDAO.getById(businessId);
        if (er == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }
        if (EqualUtil.equalsAny(er.getStatus(),
                ExpenseReimbursementStatus.RE_SUBMIT.getCode(),
                ExpenseReimbursementStatus.COMPLETED.getCode(),
                ExpenseReimbursementStatus.CANCELED.getCode(),
                ExpenseReimbursementStatus.DISCARDED.getCode(),
                ExpenseReimbursementStatus.EXPIRED.getCode())
        ) {
            throw new BusinessException("报销申请当前状态不可审批");
        }

        // 查找对应的任务
        Process process = processDAO.getByBusinessId(er.getId());
        Task task = flowableService.getTask(process.getProcessInstanceId(), dto.getApprovalTask());
        if (task == null) {
            throw new BusinessException("未找到待办任务");
        }
        String taskKey = task.getTaskDefinitionKey();

        ExpenseReimbursementApprovalType approvalType = ExpenseReimbursementApprovalType.fromTaskKey(taskKey);
        if (approvalType == null) {
            throw new BusinessException("无效的审批类型");
        }

        // 构建流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(approvalType.getExpressionKey(), dto.getApproved());
        variables.put(APPROVAL_COMMENT, dto.getComment());

        return new TaskApprovedEvent(
                PROC_DEF_KEY,
                variables,
                task.getTaskDefinitionKey(),
                task.getId(),
                dto.getApproverEmpCode()
        );
    }

    @Override
    public ExpenseReimbursementVO getById(Long id) {
        ExpenseReimbursement er = expenseReimbursementDAO.getById(id);
        if (er == null) {
            throw new BusinessException("报销申请不存在");
        }
        return toVO(er);
    }

    private void validateCreate(ExpenseReimbursementCreateDTO dto) {
        if (dto.getApplicantEmpCode() == null) {
            throw new BusinessException("申请人工号不能为空");
        }
        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("报销金额必须大于0");
        }
        if (dto.getExpenseDate() == null || dto.getExpenseDate().isBlank()) {
            throw new BusinessException("发生日期不能为空");
        }
        if (dto.getExpenseType() == null || dto.getExpenseType().isBlank()) {
            throw new BusinessException("报销类型不能为空");
        }

        // 校验申请人存在
        EmployeeBaseInfo emp = employeeBaseInfoDAO.getByEmpCode(dto.getApplicantEmpCode());
        if (emp == null) {
            throw new BusinessException("申请人不存在");
        }
    }

    private void validateUpdate(ExpenseReimbursementUpdateDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("报销申请ID不能为空");
        }
        if (dto.getExpenseDate() == null || dto.getExpenseDate().isBlank()) {
            throw new BusinessException("发生日期不能为空");
        }
        if (dto.getExpenseType() == null || dto.getExpenseType().isBlank()) {
            throw new BusinessException("报销类型不能为空");
        }
    }

    private ExpenseReimbursementVO toVO(ExpenseReimbursement e) {
        return BeanUtil.copyProperties(e, ExpenseReimbursementVO.class);
    }

}
