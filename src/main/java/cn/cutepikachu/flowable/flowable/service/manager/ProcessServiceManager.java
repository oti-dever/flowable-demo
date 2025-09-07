package cn.cutepikachu.flowable.flowable.service.manager;

import cn.cutepikachu.flowable.flowable.service.IProcessBusinessService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/6 09:18:44
 */
@Slf4j
@Component
public class ProcessServiceManager {

    /**
     * 按流程定义Key映射的流程服务
     * Key: 流程定义Key
     * Value: 该流程的服务实现
     */
    private final Map<String, IProcessBusinessService> processServiceMap = new ConcurrentHashMap<>();

    /**
     * 所有流程服务实现
     */
    @Resource
    private List<IProcessBusinessService> processServices;

    @PostConstruct
    public void initProcessServices() {
        if (processServices == null || processServices.isEmpty()) {
            log.warn("未发现任何流程服务实现");
            return;
        }
        for (IProcessBusinessService service : processServices) {
            String processDefinitionKey = service.getProcessDefinitionKey();
            if (processDefinitionKey == null || processDefinitionKey.isEmpty()) {
                log.warn("流程服务 {} 未指定流程定义Key，跳过注册", service.getClass().getName());
                continue;
            }
            if (processServiceMap.containsKey(processDefinitionKey)) {
                log.error("检测到重复的流程定义Key {}，已有服务 {}，新服务 {}，跳过注册新服务",
                        processDefinitionKey,
                        processServiceMap.get(processDefinitionKey).getClass().getName(),
                        service.getClass().getName());
                continue;
            }
            processServiceMap.put(processDefinitionKey, service);
            log.info("注册流程服务 {} 对应流程定义Key {}", service.getClass().getName(), processDefinitionKey);
        }
    }

    public IProcessBusinessService getProcessService(String processDefinitionKey) {
        return Optional.ofNullable(processServiceMap.get(processDefinitionKey))
                .orElseThrow(() -> new IllegalArgumentException("未找到对应的流程服务，流程定义Key: " + processDefinitionKey));
    }

}
