package cn.cutepikachu.flowable.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static cn.cutepikachu.flowable.constant.FlowableConstant.ExpenseReimbursement.*;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/8/28 13:52:46
 */
@Getter
@AllArgsConstructor
public enum ExpenseReimbursementApprovalType {

    DEPT_LEADER(DEPT_LEADER_TASK, "部门领导", APPROVED_DEPT_LEADER),
    BOSS(BOSS_TASK, "老板", APPROVED_BOSS),
    FINANCE(FINANCE_TASK, "财务", APPROVED_FINANCE),
    ;

    private final String taskKey;
    private final String desc;
    private final String expressionKey;

    /**
     * 根据code获取枚举
     */
    public static ExpenseReimbursementApprovalType fromTaskKey(String code) {
        for (ExpenseReimbursementApprovalType type : values()) {
            if (type.getTaskKey().equals(code)) {
                return type;
            }
        }
        return null;
    }

}
