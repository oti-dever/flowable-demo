package cn.cutepikachu.flowable.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程任务审批 DTO
 * @since 2025/9/6 15:08:32
 */
@Data
public class ProcessTaskApproveDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * TODO 通过用户上下文获取
     */
    @NotNull(message = "审批人不能为空")
    private Long approverEmpCode;

    /**
     * 审批任务
     */
    @NotBlank(message = "审批任务不能为空")
    private String approvalTask;

    /**
     * 审批意见
     */
    private Boolean approved;

    /**
     * 审批备注
     */
    private String comment;

}
