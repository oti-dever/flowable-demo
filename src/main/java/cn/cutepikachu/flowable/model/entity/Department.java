package cn.cutepikachu.flowable.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 部门实体类
 * @since 2025/8/11 13:39:09
 */
@Data
@TableName( "tb_department")
public class Department implements Serializable {
    /**
     * 部门ID
     */
    @TableId(value = "dept_id", type = IdType.ASSIGN_ID)
    private Long deptId;

    /**
     * 部门编码
     */
    @TableField(value = "dept_code")
    private String deptCode;

    /**
     * 部门名称
     */
    @TableField(value = "dept_name")
    private String deptName;

    /**
     * 部门ID路径
     */
    @TableField(value = "dept_path")
    private String deptPath;

    /**
     * 部门名称路径
     */
    @TableField(value = "dept_path_name")
    private String deptPathName;

    /**
     * 部门架构级别
     */
    @TableField(value = "dept_level")
    private Integer deptLevel;

    /**
     * 上级部门编码
     */
    @TableField(value = "parent_dept_code")
    private String parentDeptCode;

    /**
     * 部门负责人编码
     */
    @TableField(value = "dept_leader_emp_code")
    private Long deptLeaderEmpCode;

    /**
     * 岗位详细描述
     */
    @TableField(value = "description")
    private String description;

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
