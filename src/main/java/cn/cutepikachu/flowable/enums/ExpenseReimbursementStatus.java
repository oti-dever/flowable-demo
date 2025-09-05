package cn.cutepikachu.flowable.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 报销申请状态
 * @since 2025/8/28 11:32:00
 */
@Getter
@AllArgsConstructor
public enum ExpenseReimbursementStatus {
    RE_SUBMIT("RE_SUBMIT", "重新填写报销单"),
    PENDING_DEPT_APPROVAL("PENDING_DEPT_APPROVAL", "待部门主管审批"),
    PENDING_BOSS_APPROVAL("PENDING_BOSS_APPROVAL", "待老板审批"),
    PENDING_FINANCE_APPROVAL("PENDING_FINANCE_APPROVAL", "待财务审批"),
    COMPLETED("COMPLETED", "已完成"),
    CANCELED("CANCELED", "已撤销"),
    DISCARDED("DISCARDED", "已废弃"),
    EXPIRED("EXPIRED", "已过期");

    private final String code;
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static ExpenseReimbursementStatus fromCode(String code) {
        for (ExpenseReimbursementStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

}
