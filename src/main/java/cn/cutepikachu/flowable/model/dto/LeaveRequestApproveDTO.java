package cn.cutepikachu.flowable.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 审批请求DTO
 * @since 2025/8/26 16:40:00
 */
@Data
public class LeaveRequestApproveDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * TODO 通过用户上下文获取
     */
    @NotNull(message = "审批人不能为空")
    private Long approverEmpCode;

    @NotBlank
    private String approvalTask;

    @NotNull(message = "是否通过不能为空")
    private Boolean approved;

    @Size(max = 512)
    private String comment;

}

