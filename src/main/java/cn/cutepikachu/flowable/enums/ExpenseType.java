package cn.cutepikachu.flowable.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO 写成字典
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 报销类型
 * @since 2025/8/28 11:30:00
 */
@Getter
@AllArgsConstructor
public enum ExpenseType {
    TRAVEL("TRAVEL", "差旅费"),
    MEAL("MEAL", "餐费"),
    TRANSPORT("TRANSPORT", "交通费"),
    OFFICE_SUPPLIES("OFFICE_SUPPLIES", "办公用品"),
    TRAINING("TRAINING", "培训费"),
    COMMUNICATION("COMMUNICATION", "通讯费"),
    ACCOMMODATION("ACCOMMODATION", "住宿费"),
    OTHER("OTHER", "其他");

    private final String code;

    private final String desc;

}
