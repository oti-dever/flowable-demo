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
    IN_PROCESS("IN_PROCESS", "流程中"),
    PASSED("PASSED", "已通过"),
    REJECTED("REJECTED", "未通过"),
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
