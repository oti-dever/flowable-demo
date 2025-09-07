package cn.cutepikachu.flowable.dao;

import cn.cutepikachu.flowable.mapper.FlowableProcessDefinitionMapper;
import cn.cutepikachu.flowable.model.entity.FlowableProcessDefinition;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程定义表DAO
 * @since 2025/09/01 10:45:13
 */
@Service
public class FlowableProcessDefinitionDAO extends ServiceImpl<FlowableProcessDefinitionMapper, FlowableProcessDefinition> {

    /**
     * 根据流程定义ID获取流程定义
     */
    public FlowableProcessDefinition getByProcessDefinitionId(String processDefinitionId) {
        return lambdaQuery()
                .eq(FlowableProcessDefinition::getProcessDefinitionId, processDefinitionId)
                .one();
    }

    /**
     * 获取最新版本的流程定义列表
     */
    public List<FlowableProcessDefinition> listLatestProcessDefinition() {
        return baseMapper.listLatestProcessDefinition();
    }

    /**
     * 根据流程定义Key获取流程最新定义
     */
    public FlowableProcessDefinition getLatestByProcessDefinitionKey(String processDefinitionKey) {
        return lambdaQuery()
                .eq(FlowableProcessDefinition::getProcessDefinitionKey, processDefinitionKey)
                .orderByDesc(FlowableProcessDefinition::getProcessVersion)
                .last("LIMIT 1")
                .one();
    }

    /**
     * 根据流程定义Key和版本获取流程定义
     */
    public FlowableProcessDefinition getByProcessDefinitionKeyAndVersion(String processDefinitionKey, Integer processVersion) {
        return lambdaQuery()
                .eq(FlowableProcessDefinition::getProcessDefinitionKey, processDefinitionKey)
                .eq(FlowableProcessDefinition::getProcessVersion, processVersion)
                .one();
    }

    public void saveProcessDefinitions(List<ProcessDefinition> processDefinitions) {
        List<FlowableProcessDefinition> flowableProcessDefinitions = new ArrayList<>();
        processDefinitions.forEach(processDefinition -> {
            String key = processDefinition.getKey();
            int version = processDefinition.getVersion();
            boolean exists = lambdaQuery()
                    .eq(FlowableProcessDefinition::getProcessDefinitionKey, key)
                    .eq(FlowableProcessDefinition::getProcessVersion, version)
                    .exists();
            if (exists) {
                return;
            }
            FlowableProcessDefinition flowableProcessDefinition = FlowableProcessDefinition.builder()
                    .processDefinitionKey(processDefinition.getKey())
                    .processDefinitionId(processDefinition.getId())
                    .processName(processDefinition.getName())
                    .processVersion(processDefinition.getVersion())
                    .build();
            flowableProcessDefinitions.add(flowableProcessDefinition);
        });
        if (!flowableProcessDefinitions.isEmpty()) {
            saveBatch(flowableProcessDefinitions);
        }
    }

}
