package cn.cutepikachu.flowable.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/1 15:35:18
 */
@Data
public class ProcessVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 流程ID
     */
    private Long id;

    /**
     * 流程定义ID
     */
    private Long processDefinitionId;

    /**
     * 流程业务ID
     */
    private Long businessId;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 流程标题 例如：张三
     */
    private String title;

    /**
     * 流程状态（草稿：DRAFT，流程中：IN_PROGRESS，已完成：COMPLETED，已撤销：CANCELED，已废弃：DISCARDED，已过期：EXPIRED）
     */
    private String processStatus;

    /**
     * 业务数据
     */
    private String data;

    /**
     * 发起人工号
     */
    private Long createByCode;

    /**
     * 发起人姓名
     */
    private String createByName;

    /**
     * 当前审批人工号
     */
    private Map<String, List<Long>> curAssigneeCodes;

    /**
     * 当前审批人姓名
     */
    private Map<String, List<String>> curAssigneeNames;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreatedTime;

}
