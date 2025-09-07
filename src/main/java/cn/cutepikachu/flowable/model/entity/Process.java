package cn.cutepikachu.flowable.model.entity;

import cn.cutepikachu.flowable.typehander.MapStringListLongTypeHandler;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程详情表
 * @since 2025/09/01 14:01:02
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@TableName(value = "tb_process", autoResultMap = true)
public class Process implements Serializable {

    /**
     * 流程ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 流程定义ID
     */
    @TableField(value = "process_definition_id")
    private Long processDefinitionId;

    /**
     * 流程业务ID
     */
    @TableField(value = "business_id")
    private Long businessId;

    /**
     * 流程实例ID
     */
    @TableField(value = "process_instance_id")
    private String processInstanceId;

    /**
     * 流程标题 例如：张三
     */
    @TableField(value = "title")
    private String title;

    /**
     * 流程状态（草稿：DRAFT，流程中：IN_PROGRESS，已完成：COMPLETED，已撤销：CANCELED，已废弃：DISCARDED，已过期：EXPIRED）
     */
    @TableField(value = "process_status")
    private String processStatus;

    /**
     * 业务数据草稿
     */
    @TableField(value = "draft_data")
    private String draftData;

    /**
     * 发起人工号
     */
    @TableField(value = "create_by_code")
    private Long createByCode;

    /**
     * 发起人姓名
     */
    @TableField(value = "create_by_name")
    private String createByName;

    /**
     * 当前审批人工号
     */
    @TableField(value = "cur_assignee_codes", typeHandler = MapStringListLongTypeHandler.class)
    private Map<String, List<Long>> curAssigneeCodes;

    /**
     * 当前审批人姓名
     */
    @TableField(value = "cur_assignee_names", typeHandler = JacksonTypeHandler.class)
    private Map<String, List<String>> curAssigneeNames;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created_time", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreatedTime;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_last_modified_time", fill = FieldFill.INSERT_UPDATE)
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
