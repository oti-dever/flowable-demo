package cn.cutepikachu.flowable.flowable.event.impl;

import cn.cutepikachu.flowable.flowable.event.ProcessEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 任务审批事件对象
 * @since 2025/9/5 09:19:17
 */
@Getter
@AllArgsConstructor
public class TaskApprovedEvent implements ProcessEvent, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String processDefinitionKey;

    private final Map<String, Object> variables;
    private final String taskDefinitionKey;
    private final String taskId;
    private final Long approverEmpCode;

}
