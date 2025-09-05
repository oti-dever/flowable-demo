package cn.cutepikachu.flowable.flowable.listener.task;

import cn.cutepikachu.flowable.dao.LeaveRequestDAO;
import cn.cutepikachu.flowable.enums.LeaveRequestStatus;
import cn.cutepikachu.flowable.flowable.listener.AbstractTaskListener;
import cn.cutepikachu.flowable.flowable.strategy.ApproverSelectStrategyFactory;
import cn.cutepikachu.flowable.flowable.service.IProcessService;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import static cn.cutepikachu.flowable.constant.FlowableConstant.BUSINESS_ID;
import static cn.cutepikachu.flowable.constant.FlowableConstant.LeaveRequest.APPROVED;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 请假审批任务监听器，只负责业务状态更新，审批人设置由策略模式处理
 * @since 2025/8/26 18:25:00
 */
@Component
public class LeaveApprovalTaskListener extends AbstractTaskListener {

    @Getter
    @Resource
    private ApproverSelectStrategyFactory approverSelectStrategyFactory;

    @Getter
    @Resource
    private IProcessService defaultProcessService;

    @Resource
    private LeaveRequestDAO leaveRequestDAO;

    @Override
    public void onCreate(DelegateTask delegateTask) {
        Long leaveId = delegateTask.getVariable(BUSINESS_ID, Long.class);
        leaveRequestDAO.updateStatusById(leaveId, LeaveRequestStatus.PENDING.getCode());
        String assignee = delegateTask.getAssignee();
        leaveRequestDAO.updateApproverById(leaveId, assignee);
    }

    @Override
    public void onComplete(DelegateTask delegateTask) {
        Long leaveId = delegateTask.getVariable(BUSINESS_ID, Long.class);
        Boolean approved = delegateTask.getVariable(APPROVED, Boolean.class);
        String newStatus = approved ?
                LeaveRequestStatus.APPROVED.getCode() : LeaveRequestStatus.REJECTED.getCode();
        leaveRequestDAO.updateStatusById(leaveId, newStatus);
    }

}
