package cn.cutepikachu.flowable.model.entity;

import cn.cutepikachu.flowable.typehander.ListOfLongTypeHandler;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 报销申请实体
 * @since 2025/8/28 11:35:00
 */
@Data
@TableName(value = "tb_expense_reimbursement", autoResultMap = true)
public class ExpenseReimbursement implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 申请人工号（与员工表 employee_code 对应）
     */
    @TableField("applicant_emp_code")
    private Long applicantEmpCode;

    /**
     * 报销类型
     */
    @TableField("expense_type")
    private String expenseType;

    /**
     * 报销金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 报销事由
     */
    @TableField("reason")
    private String reason;

    /**
     * 发生日期
     */
    @TableField("expense_date")
    private LocalDateTime expenseDate;

    /**
     * 附件
     */
    @TableField(value = "attachments", typeHandler = JacksonTypeHandler.class)
    private List<String> attachments;

    /**
     * 部门主管工号
     */
    @TableField(value = "dept_leader_emp_code", typeHandler = ListOfLongTypeHandler.class)
    private List<Long> deptLeaderEmpCode;

    /**
     * 部门主管审批意见
     */
    @TableField("dept_approval_comment")
    private String deptApprovalComment;

    /**
     * 部门主管审批时间
     */
    @TableField("dept_approval_time")
    private LocalDateTime deptApprovalTime;

    /**
     * 老板工号（大额报销需要）
     */
    @TableField(value = "boss_emp_code", typeHandler = ListOfLongTypeHandler.class)
    private List<Long> bossEmpCode;

    /**
     * 老板审批意见
     */
    @TableField("boss_approval_comment")
    private String bossApprovalComment;

    /**
     * 老板审批时间
     */
    @TableField("boss_approval_time")
    private LocalDateTime bossApprovalTime;

    /**
     * 财务人员工号
     */
    @TableField(value = "finance_emp_codes", typeHandler = ListOfLongTypeHandler.class)
    private List<Long> financeEmpCodes;

    /**
     * 财务审批意见
     */
    @TableField("finance_approval_comment")
    private String financeApprovalComment;

    /**
     * 财务审批时间
     */
    @TableField("finance_approval_time")
    private LocalDateTime financeApprovalTime;

    /**
     * 抄送人工号（可多个，逗号分隔）
     */
    @TableField(value = "cc_emp_codes", typeHandler = ListOfLongTypeHandler.class)
    private List<Long> ccEmpCodes;

    /**
     * 状态：PENDING/DEPT_APPROVED/BOSS_APPROVED/FINANCE_APPROVED/REJECTED/CANCELED/EXPIRED
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标识
     */
    @TableField(value = "is_deleted")
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;

}
