package cn.cutepikachu.flowable.enums.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/4 10:40:52
 */
@Getter
@AllArgsConstructor
public enum ProcessErrorEnum implements ErrorEnum {
    // 流程定义不存在
    PROCESS_DEFINITION_NOT_FOUND(4004, "流程定义不存在"),
    // 流程不存在
    PROCESS_NOT_FOUND(4004, "流程不存在"),
    // 未找到待办任务
    TASK_NOT_FOUND(4004, "未找到待办任务"),
    // 流程当前状态不可撤销
    PROCESS_CANNOT_CANCELLED(4000, "流程当前状态不可撤销"),
    // 流程当前状态不可废弃
    PROCESS_CANNOT_DISCARD(4000, "流程当前状态不可废弃"),
    // 流程当前状态不可审批
    PROCESS_CANNOT_APPROVED(4000, "流程当前状态不可审批"),
    // 无权限
    NO_PERMISSION(4001, "无权限操作该流程"),
    // 任务未委派给用户
    TASK_NOT_ASSIGNED_TO_USER(4001, "任务未委派给用户或已完成"),
    ;

    private final Integer errorCode;
    private final String errorMsg;

}
