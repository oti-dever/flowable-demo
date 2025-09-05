package cn.cutepikachu.flowable.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 审批人选择策略
 * @since 2025/8/29 16:54:59
 */
@Getter
@AllArgsConstructor
public enum ApproverSelectStrategyEnum {

    NONE("none"),
    SPECIFIC("specific"),
    DEPT_LEADER("dept_leader"),
    BOSS("boss"),
    HR("hr"),
    FINANCE("finance"),
    LEAVE_DAYS_BASED("leave_days_based"),
    ;

    private final String code;

}
