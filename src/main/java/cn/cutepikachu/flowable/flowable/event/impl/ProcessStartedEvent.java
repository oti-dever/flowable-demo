package cn.cutepikachu.flowable.flowable.event.impl;

import cn.cutepikachu.flowable.flowable.event.ProcessEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程发起事件对象
 * @since 2025/9/5 09:17:37
 */
@Getter
@AllArgsConstructor
public class ProcessStartedEvent implements ProcessEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String processDefinitionKey;

    private final Long businessId;
    private final Map<String, Object> variables;

}
