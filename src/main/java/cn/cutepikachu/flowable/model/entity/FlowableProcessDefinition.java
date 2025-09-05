package cn.cutepikachu.flowable.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程定义表
 * @since 2025/09/01 10:45:13
 */
@Builder
@Data
@TableName(value ="tb_flowable_process_definition")
public class FlowableProcessDefinition implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 流程定义Key
     */
    @TableField(value = "process_definition_key")
    private String processDefinitionKey;

    /**
     * 流程定义名称
     */
    @TableField(value = "process_name")
    private String processName;

    /**
     * 流程定义版本
     */
    @TableField(value = "process_version")
    private Integer processVersion;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created_time")
    private LocalDateTime gmtCreatedTime;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_last_modified_time")
    private LocalDateTime gmtLastModifiedTime;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableField(value = "is_deleted")
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
