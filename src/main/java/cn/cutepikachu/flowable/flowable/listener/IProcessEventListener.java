package cn.cutepikachu.flowable.flowable.listener;

import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程事件监听器接口，用于监听具体某个流程的结束和取消事件
 * @since 2025/9/3 17:17:15
 */
public interface IProcessEventListener {

    /**
     * 获取监听的流程定义Key
     * 
     * @return 流程定义Key
     */
    String getProcessDefinitionKey();

    /**
     * 流程完成时的业务处理
     * 
     * @param event 流程完成事件
     */
    void onProcessCompletedBusiness(FlowableEngineEvent event);

    /**
     * 流程删除时的业务处理
     *
     * @param event 流程删除事件
     */
    void onProcessDeleteBusiness(FlowableEngineEvent event);

    /**
     * 判断是否应该处理该流程事件
     * 默认实现：检查流程定义Key是否匹配
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 是否应该处理
     */
    default boolean shouldHandle(String processDefinitionKey) {
        return getProcessDefinitionKey().equals(processDefinitionKey);
    }

}
