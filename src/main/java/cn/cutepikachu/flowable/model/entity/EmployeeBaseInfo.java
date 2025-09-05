package cn.cutepikachu.flowable.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工基本信息表
 * @TableName tb_employee_base_info
 */
@TableName(value ="tb_employee_base_info")
@Data
public class EmployeeBaseInfo implements Serializable {
    /**
     * 员工编码
     */
    @TableId(value = "emp_id", type = IdType.ASSIGN_ID)
    private Long empId;

    /**
     * 员工工号
     */
    @TableField(value = "employee_code")
    private Long employeeCode;

    /**
     * 姓名
     */
    @TableField(value = "employee_name")
    private String employeeName;

    /**
     * 部门编码
     */
    @TableField(value = "dept_code")
    private String deptCode;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created_time")
    private LocalDateTime gmtCreatedTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "gmt_last_modified_time")
    private LocalDateTime gmtLastModifiedTime;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableField(value = "is_deleted")
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}