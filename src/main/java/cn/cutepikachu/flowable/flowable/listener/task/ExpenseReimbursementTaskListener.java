package cn.cutepikachu.flowable.flowable.listener.task;

import cn.cutepikachu.flowable.dao.ExpenseReimbursementDAO;
import cn.cutepikachu.flowable.enums.ExpenseReimbursementStatus;
import cn.cutepikachu.flowable.flowable.listener.AbstractTaskListener;
import cn.cutepikachu.flowable.flowable.service.IProcessService;
import cn.cutepikachu.flowable.flowable.strategy.ApproverSelectStrategyFactory;
import cn.cutepikachu.flowable.model.entity.ExpenseReimbursement;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static cn.cutepikachu.flowable.constant.FlowableConstant.BUSINESS_ID;
import static cn.cutepikachu.flowable.constant.FlowableConstant.ExpenseReimbursement.*;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 报销审批任务监听器，负责触发通知
 * @since 2025/8/28 12:20:00
 */
@Component
public class ExpenseReimbursementTaskListener extends AbstractTaskListener {

    @Getter
    @Resource
    private ApproverSelectStrategyFactory approverSelectStrategyFactory;

    @Getter
    @Resource
    private IProcessService processService;

    @Resource
    private ExpenseReimbursementDAO expenseReimbursementDAO;

    @Override
    public void onCreate(DelegateTask delegateTask) {
        String taskKey = delegateTask.getTaskDefinitionKey();
        Long erId = delegateTask.getVariable(BUSINESS_ID, Long.class);
        switch (taskKey) {
            // 部门负责人审批任务
            case DEPT_LEADER_TASK -> onDeptLeaderApprovalTaskCreate(delegateTask, erId);
            // 老板审批任务
            case BOSS_TASK -> onBossApprovalTaskCreate(delegateTask, erId);
            // 财务审批任务
            case FINANCE_TASK -> onFinanceApprovalTaskCreate(delegateTask, erId);
        }
    }

    private void onDeptLeaderApprovalTaskCreate(DelegateTask delegateTask, Long erId) {
        String assignee = delegateTask.getAssignee();
        expenseReimbursementDAO.updateDeptLeaderById(erId, assignee);
    }

    private void onBossApprovalTaskCreate(DelegateTask delegateTask, Long erId) {
        String assignee = delegateTask.getAssignee();
        expenseReimbursementDAO.updateBossById(erId, assignee);
    }

    private void onFinanceApprovalTaskCreate(DelegateTask delegateTask, Long erId) {
        String assignee = delegateTask.getAssignee();
        expenseReimbursementDAO.updateFinanceById(erId, assignee);
    }

    @Override
    public void onComplete(DelegateTask delegateTask) {
        String taskKey = delegateTask.getTaskDefinitionKey();
        Long erId = delegateTask.getVariable(BUSINESS_ID, Long.class);
        ExpenseReimbursement er = expenseReimbursementDAO.getById(erId);
        String comment = delegateTask.getVariable(APPROVAL_COMMENT, String.class);
        Boolean approved = delegateTask.getVariable(taskKey + "_approved", Boolean.class);
        switch (taskKey) {
            // 部门负责人审批任务
            case DEPT_LEADER_TASK -> onDeptLeaderApprovalTaskComplete(delegateTask, er, approved, comment);
            // 老板审批任务
            case BOSS_TASK -> onBossApprovalTaskComplete(delegateTask, er, approved, comment);
            // 财务审批任务
            case FINANCE_TASK -> onFinanceApprovalTaskComplete(delegateTask, er, approved, comment);
        }
    }

    private void onDeptLeaderApprovalTaskComplete(DelegateTask delegateTask, ExpenseReimbursement er, Boolean approved, String comment) {
        if (!approved) {
            expenseReimbursementDAO.updateStatusById(er.getId(), ExpenseReimbursementStatus.REJECTED.getCode());
        }
        expenseReimbursementDAO.updateApprovalTimeById(er.getId(), LocalDateTime.now(), null, null);
        expenseReimbursementDAO.updateApprovalCommentById(er.getId(), comment, null, null);
    }

    private void onBossApprovalTaskComplete(DelegateTask delegateTask, ExpenseReimbursement er, Boolean approved, String comment) {
        if (!approved) {
            expenseReimbursementDAO.updateStatusById(er.getId(), ExpenseReimbursementStatus.REJECTED.getCode());
        }
        expenseReimbursementDAO.updateApprovalTimeById(er.getId(), er.getDeptApprovalTime(), LocalDateTime.now(), null);
        expenseReimbursementDAO.updateApprovalCommentById(er.getId(), er.getDeptApprovalComment(), comment, null);
    }

    private void onFinanceApprovalTaskComplete(DelegateTask delegateTask, ExpenseReimbursement er, Boolean approved, String comment) {
        String newStatus = approved ?
                ExpenseReimbursementStatus.PASSED.getCode() : ExpenseReimbursementStatus.REJECTED.getCode();
        expenseReimbursementDAO.updateApprovalTimeById(er.getId(), er.getDeptApprovalTime(), er.getBossApprovalTime(), LocalDateTime.now());
        expenseReimbursementDAO.updateStatusById(er.getId(), newStatus);
        expenseReimbursementDAO.updateApprovalCommentById(er.getId(), er.getDeptApprovalComment(), er.getBossApprovalComment(), comment);
    }

}
