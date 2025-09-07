create database if not exists `demo_db`;

use `demo_db`;

-- tb_employee_base_info: table
CREATE TABLE `tb_employee_base_info`
(
    `emp_id`                 bigint       NOT NULL COMMENT '员工编码',
    `employee_code`          bigint       NOT NULL COMMENT '员工工号',
    `employee_name`          varchar(32)  NOT NULL COMMENT '姓名',
    `dept_code`              varchar(32)           DEFAULT NULL COMMENT '部门编码',
    `email`                  varchar(100) NULL COMMENT '邮箱',
    `gmt_created_time`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_last_modified_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `is_deleted`             tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`emp_id`),
    UNIQUE KEY `uiq_employee_code` (`employee_code`),
    KEY `idx_emp_name` (`employee_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='员工基本信息表';

-- tb_department: table
CREATE TABLE `tb_department`
(
    `dept_id`                bigint      NOT NULL COMMENT '部门编码',
    `dept_level`             int                  DEFAULT NULL COMMENT '架构级别',
    `dept_code`              varchar(32) NOT NULL COMMENT '部门编号',
    `dept_name`              varchar(32) NOT NULL COMMENT '部门名称',
    `dept_path`              varchar(1024)        DEFAULT NULL COMMENT '部门ID全路径',
    `dept_path_name`         varchar(2048)        DEFAULT NULL COMMENT '部门名称全路径',
    `parent_dept_code`       varchar(32)          DEFAULT NULL COMMENT '上级部门编码',
    `dept_leader_emp_code`   bigint               DEFAULT NULL COMMENT '部门负责人编码，关联 tb_employee_base_info.employee_code',
    `description`            varchar(1024)        DEFAULT NULL COMMENT '部门详细描述',
    `gmt_created_time`       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_last_modified_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `is_deleted`             tinyint     NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`dept_id`),
    UNIQUE KEY `uiq_dept_code` (`dept_code`),
    KEY `idx_dept_name` (`dept_name`),
    KEY `idx_parent_dept_code` (`parent_dept_code`),
    KEY `idx_dept_leader_emp_code` (`dept_leader_emp_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='部门信息表';

DROP TABLE IF EXISTS `tb_flowable_process_definition`;
CREATE TABLE IF NOT EXISTS `tb_flowable_process_definition`
(
    `id`                     bigint      NOT NULL COMMENT '主键ID',
    `process_definition_key` varchar(64) NOT NULL COMMENT '流程定义Key',
    `process_definition_id`  varchar(128)         DEFAULT NULL COMMENT '流程定义ID',
    `process_name`           varchar(128)         DEFAULT NULL COMMENT '流程定义名称',
    `process_version`        int         NOT NULL COMMENT '流程定义版本',
    `gmt_created_time`       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_last_modified_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`             tinyint     NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uiq_process_definition_key_version` (`process_definition_key`, `process_version`),
    KEY `idx_process_definition_key` (`process_definition_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='流程定义表';

DROP TABLE IF EXISTS `tb_process`;
CREATE TABLE IF NOT EXISTS tb_process
(
    `id`                     BIGINT PRIMARY KEY COMMENT '流程ID',
    `process_definition_id`  BIGINT       NOT NULL COMMENT '流程定义ID',
    `title`                  VARCHAR(256) NOT NULL COMMENT '流程标题 例如：张三',
    `process_status`         VARCHAR(64)  NOT NULL COMMENT '流程状态（草稿：DRAFT，流程中：IN_PROGRESS，已完成：COMPLETED，已撤销：CANCELED，已废弃：DISCARDED，已过期：EXPIRED）',
    `business_id`            BIGINT       NULL COMMENT '流程业务ID，对应的业务表的主键ID',
    `process_instance_id`    VARCHAR(128) NULL COMMENT '流程实例ID',
    `draft_data`             TEXT         NULL COMMENT '业务数据草稿',
    `create_by_code`         BIGINT       NULL COMMENT '发起人工号',
    `create_by_name`         VARCHAR(64)  NULL COMMENT '发起人姓名',
    `cur_assignee_codes`     VARCHAR(512) NULL COMMENT '当前审批人工号',
    `cur_assignee_names`     VARCHAR(512) NULL COMMENT '当前审批人姓名',
    `gmt_created_time`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_last_modified_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `is_deleted`             tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
    UNIQUE KEY `uiq_process_instance_id` (`process_instance_id`),
    KEY `idx_process_status` (`process_status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT '流程详情表';


DROP TABLE IF EXISTS `tb_leave_request`;
CREATE TABLE IF NOT EXISTS `tb_leave_request`
(
    `id`                 bigint      NOT NULL COMMENT '主键',
    `applicant_emp_code` bigint      NOT NULL COMMENT '申请人工号',
    `leave_type`         varchar(32) NOT NULL COMMENT '请假类型',
    `start_time`         datetime    NOT NULL COMMENT '开始时间',
    `end_time`           datetime    NOT NULL COMMENT '结束时间',
    `duration_days`      int         NOT NULL COMMENT '时长(天)',
    `reason`             varchar(1024)        DEFAULT NULL COMMENT '请假原因',
    `attachments`        varchar(2048)        DEFAULT NULL COMMENT '附件(逗号分隔URL)',
    `approver_emp_codes` varchar(512)         DEFAULT NULL COMMENT '审批人工号集合(逗号分隔)',
    `cc_emp_codes`       varchar(512)         DEFAULT NULL COMMENT '抄送人工号集合(逗号分隔)',
    `status`             varchar(32) NOT NULL DEFAULT 'PENDING' COMMENT '状态',
    `create_time`        datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`         tinyint     NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`id`),
    KEY `idx_applicant_emp_code` (`applicant_emp_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='请假申请表';

DROP TABLE IF EXISTS `tb_expense_reimbursement`;
CREATE TABLE IF NOT EXISTS `tb_expense_reimbursement`
(
    `id`                       bigint         NOT NULL COMMENT '主键ID',
    `applicant_emp_code`       bigint         NOT NULL COMMENT '申请人工号',
    `expense_type`             varchar(32)    NOT NULL COMMENT '报销类型',
    `amount`                   decimal(10, 2) NOT NULL COMMENT '报销金额',
    `reason`                   varchar(500)   NOT NULL COMMENT '报销事由',
    `expense_date`             datetime       NOT NULL COMMENT '发生日期',
    `attachments`              varchar(2048)           DEFAULT NULL COMMENT '附件列表',
    `dept_leader_emp_code`     varchar(512)            DEFAULT NULL COMMENT '部门主管工号',
    `dept_approval_comment`    varchar(512)            DEFAULT NULL COMMENT '部门主管审批意见',
    `dept_approval_time`       datetime                DEFAULT NULL COMMENT '部门主管审批时间',
    `boss_emp_code`            varchar(512)            DEFAULT NULL COMMENT '老板工号',
    `boss_approval_comment`    varchar(512)            DEFAULT NULL COMMENT '老板审批意见',
    `boss_approval_time`       datetime                DEFAULT NULL COMMENT '老板审批时间',
    `finance_emp_codes`        varchar(512)            DEFAULT NULL COMMENT '财务审批人员工号',
    `finance_approval_comment` varchar(512)            DEFAULT NULL COMMENT '财务审批意见',
    `finance_approval_time`    datetime                DEFAULT NULL COMMENT '财务审批时间',
    `cc_emp_codes`             varchar(512)            DEFAULT NULL COMMENT '抄送人工号列表（逗号分隔）',
    `status`                   varchar(32)    NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING/DEPT_APPROVED/BOSS_APPROVED/FINANCE_APPROVED/REJECTED/CANCELED/EXPIRED',
    `create_time`              datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`               tinyint        NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`id`),
    KEY `idx_applicant_emp_code` (`applicant_emp_code`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='报销申请表';

-- 测试 ForkJoin 流程
DROP TABLE IF EXISTS `tb_test_fork_join`;
CREATE TABLE IF NOT EXISTS `tb_test_fork_join`
(
    `id`                     bigint      NOT NULL COMMENT '主键ID',
    `applicant_emp_code`     bigint      NOT NULL COMMENT '申请人工号',
    `task1_assignees`        varchar(512)         DEFAULT NULL COMMENT '任务1审批人工号集合(逗号分隔)',
    `task2_assignees`        varchar(512)         DEFAULT NULL COMMENT '任务2审批人工号集合(逗号分隔)',
    `task3_assignees`        varchar(512)         DEFAULT NULL COMMENT '任务3审批人工号集合(逗号分隔)',
    `task4_assignees`        varchar(512)         DEFAULT NULL COMMENT '任务4审批人工号集合(逗号分隔)',
    `status`                 varchar(32) NOT NULL DEFAULT 'PENDING' COMMENT '状态',
    `gmt_created_time`       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_last_modified_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`             tinyint     NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='测试 ForkJoin 流程表';

