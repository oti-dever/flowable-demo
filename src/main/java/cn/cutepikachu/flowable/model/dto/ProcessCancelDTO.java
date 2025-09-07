package cn.cutepikachu.flowable.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程撤销 DTO
 * @since 2025/9/2 09:34:33
 */
@Data
public class ProcessCancelDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * TODO 通过用户上下文获取
     */
    @NotNull(message = "申请人工号不能为空")
    private Long applicantEmpCode;

    @Size(max = 256)
    private String comment;

}
