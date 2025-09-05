package cn.cutepikachu.flowable.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 请假申请状态
 * @since 2025/8/28 17:18:00
 */
@Getter
@AllArgsConstructor
public enum LeaveRequestStatus {
    PENDING("PENDING", "待审批"),
    APPROVED("APPROVED", "已批准"),
    REJECTED("REJECTED", "已拒绝"),
    CANCELED("CANCELED", "已撤销"),
    DISCARDED("DISCARDED", "已废弃"),
    EXPIRED("EXPIRED", "已过期");

    private final String code;
    private final String desc;

}
