package cn.cutepikachu.flowable.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO 写成字典
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 请假类型
 * @since 2025/8/26 16:00:00
 */
@Getter
@AllArgsConstructor
public enum LeaveType {
    ANNUAL("ANNUAL", "年假"),
    SICK("SICK", "病假"),
    CASUAL("CASUAL", "事假"),
    MARRIAGE("MARRIAGE", "婚假"),
    MATERNITY("MATERNITY", "产假"),
    PATERNITY("PATERNITY", "陪产假"),
    FUNERAL("FUNERAL", "丧假"),
    OTHER("OTHER", "其他");

    private final String code;

    private final String desc;

}

