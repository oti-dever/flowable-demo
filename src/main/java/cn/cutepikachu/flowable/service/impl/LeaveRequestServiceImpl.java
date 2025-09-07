package cn.cutepikachu.flowable.service.impl;

import cn.cutepikachu.flowable.constant.FlowableConstant;
import cn.cutepikachu.flowable.dao.EmployeeBaseInfoDAO;
import cn.cutepikachu.flowable.dao.LeaveRequestDAO;
import cn.cutepikachu.flowable.enums.ApproverSelectStrategyEnum;
import cn.cutepikachu.flowable.enums.LeaveRequestStatus;
import cn.cutepikachu.flowable.enums.error.ProcessErrorEnum;
import cn.cutepikachu.flowable.exception.BusinessException;
import cn.cutepikachu.flowable.flowable.event.impl.ProcessStartedEvent;
import cn.cutepikachu.flowable.flowable.event.impl.TaskApprovedEvent;
import cn.cutepikachu.flowable.model.dto.LeaveRequestCreateDTO;
import cn.cutepikachu.flowable.model.dto.ProcessTaskApproveDTO;
import cn.cutepikachu.flowable.model.entity.EmployeeBaseInfo;
import cn.cutepikachu.flowable.model.entity.LeaveRequest;
import cn.cutepikachu.flowable.model.vo.LeaveRequestVO;
import cn.cutepikachu.flowable.service.LeaveRequestService;
import cn.cutepikachu.flowable.util.ListOfTypeUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static cn.cutepikachu.flowable.constant.FlowableConstant.EXPIRE_DURATION;
import static cn.cutepikachu.flowable.constant.FlowableConstant.LeaveRequest.*;
import static cn.cutepikachu.flowable.constant.FlowableConstant.STARTER;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 请假服务实现
 * @since 2025/8/26 17:25:00
 */
@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    @Resource
    private EmployeeBaseInfoDAO employeeBaseInfoDAO;

    @Resource
    private LeaveRequestDAO leaveRequestDAO;

    @Override
    public String getProcessDefinitionKey() {
        return PROC_DEF_KEY;
    }

    @Override
    public Object getByBusinessData(Long businessId) {
        LeaveRequest lr = leaveRequestDAO.getById(businessId);
        if (lr == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }
        return toVO(lr);
    }

    @Override
    public <T> ProcessStartedEvent startProcessBusiness(Long processId, T startDTO) {
        LeaveRequestCreateDTO dto;
        try {
            dto = BeanUtil.toBean(startDTO, LeaveRequestCreateDTO.class);
        } catch (Exception e) {
            throw new BusinessException("参数类型错误");
        }
        validateCreate(dto);
        LocalDateTime startTime = LocalDateTimeUtil.parse(dto.getStartTime());
        LocalDateTime endTime = LocalDateTimeUtil.parse(dto.getEndTime());
        int days = (int) (ChronoUnit.DAYS.between(startTime, endTime) + 1);
        if (days <= 0) {
            throw new BusinessException("开始日期不能晚于结束日期");
        }

        LeaveRequest entity = new LeaveRequest();
        entity.setApplicantEmpCode(dto.getApplicantEmpCode());
        entity.setLeaveType(dto.getLeaveType());
        entity.setStartTime(startTime);
        entity.setEndTime(endTime);
        entity.setDurationDays(days);
        entity.setReason(dto.getReason());
        entity.setAttachments(dto.getAttachments());
        entity.setCcEmpCodes(dto.getCcEmpCodes());
        entity.setStatus(LeaveRequestStatus.IN_PROCESS.getCode());

        boolean saved = leaveRequestDAO.save(entity);
        if (!saved) {
            throw new BusinessException("请假单保存失败");
        }
        Long businessId = entity.getId();

        // 构建流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(STARTER, dto.getApplicantEmpCode());
        String expireDuration = Duration.ofDays(days).toString();
        // 测试 1 分钟过期
        // String expireDuration = Duration.ofMinutes(1).toString();
        variables.put(EXPIRE_DURATION, expireDuration);

        // 如果指定了审批人列表，则添加到流程变量中，并修改策略为 SPECIFIC
        if (dto.getApproverEmpCode() != null && !dto.getApproverEmpCode().isEmpty()) {
            variables.put(FlowableConstant.SPECIFIC_APPROVERS, Map.of(APPROVAL_TASK, ListOfTypeUtil.toString(dto.getApproverEmpCode())));
            Map<String, ApproverSelectStrategyEnum> specificStrategyMap = new HashMap<>();
            specificStrategyMap.put(APPROVAL_TASK, ApproverSelectStrategyEnum.SPECIFIC);
            variables.put(FlowableConstant.ASSIGNEE_STRATEGY, specificStrategyMap);
        } else {
            // 默认策略
            variables.put(FlowableConstant.ASSIGNEE_STRATEGY, ASSIGNEE_STRATEGY_MAP);
        }

        return new ProcessStartedEvent(
                PROC_DEF_KEY,
                businessId,
                variables
        );
    }

    @Override
    public Boolean cancelProcessBusiness(Long businessId, String comment) {
        LeaveRequest lr = leaveRequestDAO.getById(businessId);
        if (lr == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }

        leaveRequestDAO.updateStatusById(businessId, LeaveRequestStatus.CANCELED.getCode());
        return Boolean.TRUE;
    }

    @Override
    public Boolean discardProcessBusiness(Long businessId, String comment) {
        LeaveRequest lr = leaveRequestDAO.getById(businessId);
        if (lr == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }

        leaveRequestDAO.updateStatusById(businessId, LeaveRequestStatus.DISCARDED.getCode());
        return Boolean.TRUE;
    }

    @Override
    public TaskApprovedEvent approveTaskBusiness(Long businessId, ProcessTaskApproveDTO dto) {
        LeaveRequest lr = leaveRequestDAO.getById(businessId);
        if (lr == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }

        // 构建流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(APPROVED, dto.getApproved());

        return new TaskApprovedEvent(
                PROC_DEF_KEY,
                variables
        );
    }

    private void validateCreate(LeaveRequestCreateDTO dto) {
        if (dto.getApplicantEmpCode() == null) {
            throw new BusinessException("申请人工号不能为空");
        }
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new BusinessException("开始和结束日期不能为空");
        }
        if (dto.getLeaveType() == null || dto.getLeaveType().isBlank()) {
            throw new BusinessException("请假类型不能为空");
        }
        // 校验申请人存在
        EmployeeBaseInfo emp = employeeBaseInfoDAO.getByEmpCode(dto.getApplicantEmpCode());
        if (emp == null) {
            throw new BusinessException("申请人不存在");
        }
    }

    private LeaveRequestVO toVO(LeaveRequest e) {
        return BeanUtil.copyProperties(e, LeaveRequestVO.class);
    }

}
