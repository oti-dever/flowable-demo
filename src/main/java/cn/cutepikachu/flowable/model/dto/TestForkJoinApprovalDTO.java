package cn.cutepikachu.flowable.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/3 10:00:39
 */
@Data
public class TestForkJoinApprovalDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private Long approverEmpCode;

    @NotBlank
    private String approvalTask;

}
