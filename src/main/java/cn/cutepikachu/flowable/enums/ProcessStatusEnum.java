package cn.cutepikachu.flowable.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/1 13:46:14
 */
@Getter
@AllArgsConstructor
public enum ProcessStatusEnum {
    DRAFT("DRAFT", "草稿"),
    IN_PROGRESS("IN_PROGRESS", "流程中"),
    COMPLETED("COMPLETED", "已完成"),
    CANCELED("CANCELED", "已撤销"),
    DISCARDED("DISCARDED", "已废弃"),
    EXPIRED("EXPIRED", "已过期"),
    ;

    private final String code;
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static ProcessStatusEnum fromCode(String code) {
        for (ProcessStatusEnum status : ProcessStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

}
