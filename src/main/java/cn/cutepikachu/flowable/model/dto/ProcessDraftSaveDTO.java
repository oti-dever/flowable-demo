package cn.cutepikachu.flowable.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程草稿保存 DTO
 * @since 2025/9/1 15:39:15
 */
@Data
public class ProcessDraftSaveDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 流程ID
     */
    private Long id;

    /**
     * 流程定义ID
     */
    @NotNull(message = "流程定义ID不能为空")
    private Long processDefinitionId;

    /**
     * 流程标题 例如：张三
     */
    @NotBlank(message = "流程标题不能为空")
    private String title;

    /**
     * 业务数据草稿
     */
    private String draftData;

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
