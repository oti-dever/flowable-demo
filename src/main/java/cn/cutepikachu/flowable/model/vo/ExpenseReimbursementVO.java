package cn.cutepikachu.flowable.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 报销申请VO
 * @since 2025/8/28 11:55:00
 */
@Data
public class ExpenseReimbursementVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long applicantEmpCode;

    private String expenseType;

    private BigDecimal amount;

    private String reason;

    private LocalDateTime expenseDate;

    private List<String> attachments;

    private List<Long> deptLeaderEmpCode;

    private String deptApprovalComment;

    private LocalDateTime deptApprovalTime;

    private List<Long> bossEmpCode;

    private String bossApprovalComment;

    private LocalDateTime bossApprovalTime;

    private List<Long> financeEmpCode;

    private String financeApprovalComment;

    private LocalDateTime financeApprovalTime;

    private List<Long> ccEmpCodes;

    private String status;

    private LocalDateTime createTime;

}
