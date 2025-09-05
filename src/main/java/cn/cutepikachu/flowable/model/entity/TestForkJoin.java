package cn.cutepikachu.flowable.model.entity;

import cn.cutepikachu.flowable.typehander.ListOfLongTypeHandler;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 测试 ForkJoin 流程实体
 * @since 2025/09/03 09:54:31
 */
@Data
@TableName(value = "tb_test_fork_join", autoResultMap = true)
public class TestForkJoin implements Serializable {

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
     * 任务1审批人工号集合(逗号分隔)
     */
    @TableField(value = "task1_assignees", typeHandler = ListOfLongTypeHandler.class)
    private List<Long> task1Assignees;

    /**
     * 任务2审批人工号集合(逗号分隔)
     */
    @TableField(value = "task2_assignees", typeHandler = ListOfLongTypeHandler.class)
    private List<Long> task2Assignees;

    /**
     * 任务3审批人工号集合(逗号分隔)
     */
    @TableField(value = "task3_assignees", typeHandler = ListOfLongTypeHandler.class)
    private List<Long> task3Assignees;

    /**
     * 任务4审批人工号集合(逗号分隔)
     */
    @TableField(value = "task4_assignees", typeHandler = ListOfLongTypeHandler.class)
    private List<Long> task4Assignees;

    /**
     * 状态
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created_time", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreatedTime;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_last_modified_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtLastModifiedTime;

    /**
     * 逻辑删除标识
     */
    @TableField(value = "is_deleted")
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;

}
