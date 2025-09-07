package cn.cutepikachu.flowable.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/1 11:05:11
 */
@Data
public class FlowableProcessDefinitionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 流程定义Key
     */
    private String processDefinitionKey;

    /**
     * 流程定义ID
     */
    private String processDefinitionId;

    /**
     * 流程定义名称
     */
    private String processName;

    /**
     * 流程定义版本
     */
    private Integer processVersion;

}
