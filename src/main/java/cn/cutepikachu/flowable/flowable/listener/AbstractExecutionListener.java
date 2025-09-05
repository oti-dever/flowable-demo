package cn.cutepikachu.flowable.flowable.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 执行事件监听器抽象类
 * @since 2025/8/27 15:47:52
 */
@Slf4j
public class AbstractExecutionListener implements ExecutionListener {

    @Override
    public final void notify(DelegateExecution execution) {
        String event = execution.getEventName();
        switch (event) {
            case EVENTNAME_START -> {
                onStart(execution);
                notifyStart(execution);
            }
            case EVENTNAME_END -> {
                onEnd(execution);
                notifyEnd(execution);
            }
            case EVENTNAME_TAKE -> {
                onTake(execution);
                notifyTake(execution);
            }
            default -> {
            }
        }
    }

    /**
     * 执行事件开始
     *
     * @param execution 执行事件
     */
    protected void onStart(DelegateExecution execution) {
    }

    /**
     * 执行事件结束
     *
     * @param execution 执行事件
     */
    protected void onEnd(DelegateExecution execution) {
    }

    /**
     * 执行事件流转
     *
     * @param execution 执行事件
     */
    protected void onTake(DelegateExecution execution) {
    }

    protected void notifyStart(DelegateExecution execution) {
        log.info("执行事件开始: 执行ID={}, 流程实例ID={}, 流程定义ID={}, 当前活动ID={}",
                execution.getId(), execution.getProcessInstanceId(), execution.getProcessDefinitionId(), execution.getCurrentActivityId());
    }

    protected void notifyEnd(DelegateExecution execution) {
        log.info("执行事件结束: 执行ID={}, 流程实例ID={}, 流程定义ID={}, 当前活动ID={}",
                execution.getId(), execution.getProcessInstanceId(), execution.getProcessDefinitionId(), execution.getCurrentActivityId());
    }

    protected void notifyTake(DelegateExecution execution) {
        log.info("执行事件流转: 执行ID={}, 流程实例ID={}, 流程定义ID={}, 当前活动ID={}",
                execution.getId(), execution.getProcessInstanceId(), execution.getProcessDefinitionId(), execution.getCurrentActivityId());
    }

}
