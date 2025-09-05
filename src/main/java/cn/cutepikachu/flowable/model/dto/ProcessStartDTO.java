package cn.cutepikachu.flowable.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/1 15:39:15
 */
@Data
public class ProcessStartDTO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 流程ID
     */
    private Long processId;

    /**
     * 流程定义ID
     */
    private Long processDefinitionId;

    /**
     * 流程标题 例如：张三
     */
    private String title;

    /**
     * 业务数据
     */
    private T data;

    /**
     * TODO 通过用户上下文获取
     * 发起人工号
     */
    private Long createByCode;

    /**
     * TODO 通过用户上下文获取
     * 发起人姓名
     */
    private String createByName;

}
