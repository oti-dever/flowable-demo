package cn.cutepikachu.flowable.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/3 16:59:37
 */
@Data
public class TestForkJoinCreateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private Long applicantEmpCode;

    @NotEmpty
    private List<Long> task1Assignees;

    @NotEmpty
    private List<Long> task2Assignees;

    @NotEmpty
    private List<Long> task3Assignees;

    @NotEmpty
    private List<Long> task4Assignees;

}
