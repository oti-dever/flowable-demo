package cn.cutepikachu.flowable.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 报销申请创建DTO
 * @since 2025/8/28 11:45:00
 */
@Data
public class ExpenseReimbursementCreateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * TODO 通过用户上下文获取
     * 申请人工号
     */
    @NotNull(message = "申请人工号不能为空")
    private Long applicantEmpCode;

    /**
     * 报销类型
     */
    @NotBlank(message = "报销类型不能为空")
    private String expenseType;

    /**
     * 报销金额
     */
    @NotNull(message = "报销金额不能为空")
    @DecimalMin(value = "0.01", message = "报销金额必须大于0")
    private BigDecimal amount;

    /**
     * 报销事由
     */
    @NotBlank(message = "报销事由不能为空")
    private String reason;

    /**
     * 发生日期
     */
    @NotBlank(message = "发生日期不能为空")
    private String expenseDate;

    /**
     * 附件列表
     */
    private List<String> attachments;

    /**
     * 抄送人工号列表
     */
    private List<Long> ccEmpCodes;

}
