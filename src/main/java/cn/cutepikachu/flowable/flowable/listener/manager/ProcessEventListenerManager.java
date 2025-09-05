package cn.cutepikachu.flowable.flowable.listener.manager;

import cn.cutepikachu.flowable.flowable.listener.IProcessEventListener;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.delegate.event.FlowableCancelledEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程事件监听器管理器，负责管理和分发特定流程事件
 * @since 2025/9/3 17:17:15
 */
@Slf4j
@Component
public class ProcessEventListenerManager {

    /**
     * 按流程定义Key分组的监听器映射
     * Key: 流程定义Key
     * Value: 该流程的监听器列表
     */
    private final Map<String, List<IProcessEventListener>> listenerMap = new ConcurrentHashMap<>();

    /**
     * 所有特定流程事件监听器
     */
    private List<IProcessEventListener> specificProcessEventListeners;

    /**
     * 注入所有实现了ISpecificProcessEventListener接口的Bean
     */
    @Autowired(required = false)
    public void setSpecificProcessEventListeners(List<IProcessEventListener> listeners) {
        this.specificProcessEventListeners = listeners;
    }

    /**
     * 初始化监听器映射
     */
    @PostConstruct
    public void initializeListeners() {
        if (specificProcessEventListeners == null || specificProcessEventListeners.isEmpty()) {
            log.info("未发现任何特定流程事件监听器");
            return;
        }

        // 按流程定义Key分组
        listenerMap.putAll(
            specificProcessEventListeners.stream()
                .collect(Collectors.groupingBy(IProcessEventListener::getProcessDefinitionKey))
        );

        log.info("已注册特定流程事件监听器: {}", 
            listenerMap.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey, 
                    entry -> entry.getValue().size()
                ))
        );
    }

    /**
     * 处理流程完成事件
     * 
     * @param event 流程完成事件
     */
    public void handleProcessCompletedBusiness(FlowableEngineEntityEvent event) {
        String processDefinitionKey = extractProcessDefinitionKey(event.getProcessDefinitionId());
        String processInstanceId = event.getProcessInstanceId();
        
        List<IProcessEventListener> listeners = listenerMap.get(processDefinitionKey);
        if (listeners == null || listeners.isEmpty()) {
            log.debug("流程定义Key [{}] 没有注册特定流程事件监听器", processDefinitionKey);
            return;
        }

        for (IProcessEventListener listener : listeners) {
            try {
                if (listener.shouldHandle(processDefinitionKey)) {
                    log.debug("执行流程完成监听器: {} for 流程实例: {}", 
                        listener.getClass().getSimpleName(), processInstanceId);
                    listener.onProcessCompletedBusiness(event);
                }
            } catch (Exception e) {
                log.error("执行流程完成监听器失败: {} for 流程实例: {}", 
                    listener.getClass().getSimpleName(), processInstanceId, e);
                // 继续执行其他监听器，不因为一个监听器失败而影响其他监听器
            }
        }
    }

    /**
     * 处理流程删除事件
     *
     * @param event 流程删除事件
     */
    public void handleProcessCancelledBusiness(FlowableCancelledEvent event) {
        String processDefinitionKey = extractProcessDefinitionKey(event.getProcessDefinitionId());
        String processInstanceId = event.getProcessInstanceId();

        List<IProcessEventListener> listeners = listenerMap.get(processDefinitionKey);
        if (listeners == null || listeners.isEmpty()) {
            log.debug("流程定义Key [{}] 没有注册特定流程事件监听器", processDefinitionKey);
            return;
        }

        for (IProcessEventListener listener : listeners) {
            try {
                if (listener.shouldHandle(processDefinitionKey)) {
                    log.debug("执行流程删除监听器: {} for 流程实例: {}",
                            listener.getClass().getSimpleName(), processInstanceId);
                    listener.onProcessDeleteBusiness(event);
                }
            } catch (Exception e) {
                log.error("执行流程删除监听器失败: {} for 流程实例: {}",
                        listener.getClass().getSimpleName(), processInstanceId, e);
                // 继续执行其他监听器，不因为一个监听器失败而影响其他监听器
            }
        }
    }

    /**
     * 从流程定义ID中提取流程定义Key
     * 流程定义ID格式通常为: processKey:version:deploymentId
     * 
     * @param processDefinitionId 流程定义ID
     * @return 流程定义Key
     */
    private String extractProcessDefinitionKey(String processDefinitionId) {
        if (processDefinitionId == null) {
            return null;
        }
        
        int firstColonIndex = processDefinitionId.indexOf(':');
        if (firstColonIndex > 0) {
            return processDefinitionId.substring(0, firstColonIndex);
        }
        
        return processDefinitionId;
    }

    /**
     * 获取指定流程定义Key的监听器数量
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 监听器数量
     */
    public int getListenerCount(String processDefinitionKey) {
        List<IProcessEventListener> listeners = listenerMap.get(processDefinitionKey);
        return listeners == null ? 0 : listeners.size();
    }

    /**
     * 获取所有已注册的流程定义Key
     * 
     * @return 流程定义Key集合
     */
    public Set<String> getRegisteredProcessDefinitionKeys() {
        return listenerMap.keySet();
    }

}
