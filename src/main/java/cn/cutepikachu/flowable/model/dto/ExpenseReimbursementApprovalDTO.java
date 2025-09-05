package cn.cutepikachu.flowable.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 报销审批DTO
 * @since 2025/8/28 11:47:00
 */
@Data
public class ExpenseReimbursementApprovalDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * TODO 通过用户上下文获取
     * 审批人工号
     */
    @NotNull(message = "审批人工号不能为空")
    private Long approverEmpCode;

    /**
     * 是否通过
     */
    @NotNull(message = "审批结果不能为空")
    private Boolean approved;

    @NotBlank
    private String approvalTask;

    /**
     * 审批意见
     */
    private String comment;

}
