package cn.cutepikachu.flowable.service.impl;

import cn.cutepikachu.flowable.constant.FlowableConstant;
import cn.cutepikachu.flowable.dao.EmployeeBaseInfoDAO;
import cn.cutepikachu.flowable.dao.ExpenseReimbursementDAO;
import cn.cutepikachu.flowable.enums.ExpenseReimbursementStatus;
import cn.cutepikachu.flowable.enums.error.ProcessErrorEnum;
import cn.cutepikachu.flowable.exception.BusinessException;
import cn.cutepikachu.flowable.flowable.event.impl.ProcessStartedEvent;
import cn.cutepikachu.flowable.flowable.event.impl.TaskApprovedEvent;
import cn.cutepikachu.flowable.model.dto.ExpenseReimbursementCreateDTO;
import cn.cutepikachu.flowable.model.dto.ProcessTaskApproveDTO;
import cn.cutepikachu.flowable.model.entity.EmployeeBaseInfo;
import cn.cutepikachu.flowable.model.entity.ExpenseReimbursement;
import cn.cutepikachu.flowable.model.vo.ExpenseReimbursementVO;
import cn.cutepikachu.flowable.service.ExpenseReimbursementService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static cn.cutepikachu.flowable.constant.FlowableConstant.EXPIRE_DURATION;
import static cn.cutepikachu.flowable.constant.FlowableConstant.ExpenseReimbursement.*;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 报销申请服务实现
 * @since 2025/8/28 12:00:00
 */
@Service
public class ExpenseReimbursementServiceImpl implements ExpenseReimbursementService {

    @Resource
    private EmployeeBaseInfoDAO employeeBaseInfoDAO;

    @Resource
    private ExpenseReimbursementDAO expenseReimbursementDAO;

    @Override
    public String getProcessDefinitionKey() {
        return PROC_DEF_KEY;
    }

    @Override
    public Object getByBusinessData(Long businessId) {
        ExpenseReimbursement er = expenseReimbursementDAO.getById(businessId);
        if (er == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }
        return this.toVO(er);
    }

    @Override
    public <T> ProcessStartedEvent startProcessBusiness(Long processId, T startDTO) {
        ExpenseReimbursementCreateDTO dto;
        try {
            dto = BeanUtil.toBean(startDTO, ExpenseReimbursementCreateDTO.class);
        } catch (Exception e) {
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
        entity.setStatus(ExpenseReimbursementStatus.IN_PROCESS.getCode());

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

        expenseReimbursementDAO.updateStatusById(businessId, ExpenseReimbursementStatus.CANCELED.getCode());
        return Boolean.TRUE;
    }

    @Override
    public Boolean discardProcessBusiness(Long businessId, String comment) {
        ExpenseReimbursement er = expenseReimbursementDAO.getById(businessId);
        if (er == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }

        expenseReimbursementDAO.updateStatusById(businessId, ExpenseReimbursementStatus.DISCARDED.getCode());
        return Boolean.TRUE;
    }

    @Override
    public TaskApprovedEvent approveTaskBusiness(Long businessId, ProcessTaskApproveDTO dto) {
        ExpenseReimbursement er = expenseReimbursementDAO.getById(businessId);
        if (er == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }

        // 构建流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(dto.getApprovalTask() + "_approved", dto.getApproved());
        variables.put(APPROVED, dto.getApproved());
        variables.put(APPROVAL_COMMENT, dto.getComment());

        return new TaskApprovedEvent(
                PROC_DEF_KEY,
                variables
        );
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

    private ExpenseReimbursementVO toVO(ExpenseReimbursement e) {
        return BeanUtil.copyProperties(e, ExpenseReimbursementVO.class);
    }

}
