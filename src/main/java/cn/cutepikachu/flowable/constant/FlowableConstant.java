package cn.cutepikachu.flowable.constant;

import cn.cutepikachu.flowable.enums.ApproverSelectStrategyEnum;

import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/8/28 11:08:34
 */
public interface FlowableConstant {

    /**
     * 发起人
     */
    String STARTER = "starter";

    /**
     * 审批人选择策略
     */
    String ASSIGNEE_STRATEGY = "assigneeStrategy";

    /**
     * 指定审批人列表
     * <p>
     * Key: 任务 Key
     * Value: 审批人编码列表（`,` 号分隔）
     */
    String SPECIFIC_APPROVERS = "specificApprovers";

    /**
     * 流程ID
     */
    String PROCESS_ID = "processId";

    /**
     * 业务ID
     */
    String BUSINESS_ID = "businessId";

    /**
     * 过期时间间隔
     */
    String EXPIRE_DURATION = "expireDuration";

    /**
     * 请假流程
     */
    interface LeaveRequest {
        String PROC_DEF_KEY = "leave_request_process";
        /**
         * 审批是否通过
         */
        String APPROVED = "approved";
        /**
         * 审批任务
         */
        String APPROVAL_TASK = "approveTask";
        /**
         * 审批人选择策略映射
         * <p>
         * Key: 任务 Key
         * Value: 策略
         */
        Map<String, ApproverSelectStrategyEnum> ASSIGNEE_STRATEGY_MAP = Map.of(
                APPROVAL_TASK, ApproverSelectStrategyEnum.LEAVE_DAYS_BASED
        );
    }

    /**
     * 费用报销流程
     */
    interface ExpenseReimbursement {
        String PROC_DEF_KEY = "expense_reimbursement_process";
        /**
         * 审批意见
         */
        String APPROVAL_COMMENT = "approvalComment";
        /**
         * 部门主管审批任务
         */
        String DEPT_LEADER_TASK = "deptLeaderApprovalTask";
        /**
         * 老板审批任务
         */
        String BOSS_TASK = "bossApprovalTask";
        /**
         * 财务审批任务
         */
        String FINANCE_TASK = "financeApprovalTask";
        /**
         * 报销金额
         */
        String AMOUNT = "amount";
        /**
         * 审批是否通过
         */
        String APPROVED = "approved";
        /**
         * 审批人选择策略映射
         * <p>
         * Key: 任务 Key
         * Value: 策略
         */
        Map<String, ApproverSelectStrategyEnum> ASSIGNEE_STRATEGY_MAP = Map.of(
                DEPT_LEADER_TASK, ApproverSelectStrategyEnum.DEPT_LEADER,
                BOSS_TASK, ApproverSelectStrategyEnum.BOSS,
                FINANCE_TASK, ApproverSelectStrategyEnum.FINANCE
        );
    }

    /**
     * 测试 ForkJoin 流程
     */
    interface TestForkJoin {
        String PROC_DEF_KEY = "UnbalancedForkJoin";
        /**
         * 任务 1
         */
        String TASK_1 = "task1";
        /**
         * 任务 2
         */
        String TASK_2 = "task2";
        /**
         * 任务 3
         */
        String TASK_3 = "task3";
        /**
         * 任务 4
         */
        String TASK_4 = "task4";
        /**
         * 审批是否通过
         */
        String APPROVED = "approved";
        /**
         * 审批人选择策略映射
         * <p>
         * Key: 任务 Key
         * Value: 策略
         */
        Map<String, ApproverSelectStrategyEnum> ASSIGNEE_STRATEGY_MAP = Map.of(
                TASK_1, ApproverSelectStrategyEnum.SPECIFIC,
                TASK_2, ApproverSelectStrategyEnum.SPECIFIC,
                TASK_3, ApproverSelectStrategyEnum.SPECIFIC,
                TASK_4, ApproverSelectStrategyEnum.SPECIFIC
        );
    }

}
