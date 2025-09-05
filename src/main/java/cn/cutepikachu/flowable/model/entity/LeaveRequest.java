package cn.cutepikachu.flowable.model.entity;

import cn.cutepikachu.flowable.typehander.ListOfLongTypeHandler;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 请假申请实体
 * @since 2025/8/26 16:05:00
 */
@Data
@TableName(value = "tb_leave_request", autoResultMap = true)
public class LeaveRequest implements Serializable {

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
     * 请假类型
     */
    @TableField("leave_type")
    private String leaveType;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 时长（天）
     */
    @TableField("duration_days")
    private Integer durationDays;

    /**
     * 请假原因
     */
    @TableField("reason")
    private String reason;

    /**
     * 附件，逗号分隔的URL或文件标识
     */
    @TableField(value = "attachments", typeHandler = JacksonTypeHandler.class)
    private List<String> attachments;

    /**
     * 审批人工号（可多个，逗号分隔），记录当次流程的实际审批人候选集合
     */
    @TableField(value = "approver_emp_codes", typeHandler = ListOfLongTypeHandler.class)
    private List<Long> approverEmpCodes;

    /**
     * 抄送人工号（可多个，逗号分隔）
     */
    @TableField(value = "cc_emp_codes", typeHandler = ListOfLongTypeHandler.class)
    private List<Long> ccEmpCodes;

    /**
     * 状态：PENDING/APPROVED/REJECTED/CANCELED/EXPIRED
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

