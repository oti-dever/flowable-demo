package cn.cutepikachu.flowable.flowable.service.impl;

import cn.cutepikachu.flowable.dao.FlowableProcessDefinitionDAO;
import cn.cutepikachu.flowable.flowable.service.IFlowableService;
import cn.cutepikachu.flowable.model.entity.FlowableProcessDefinition;
import jakarta.annotation.Resource;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/1 09:43:31
 */
@Service
public class FlowableServiceImpl implements IFlowableService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private FlowableProcessDefinitionDAO flowableProcessDefinitionDAO;

    @Override
    public FlowableProcessDefinition getProcessDefinition(Long processDefinitionId) {
        return flowableProcessDefinitionDAO.getById(processDefinitionId);
    }

    @Override
    public <BusinessKey> ProcessInstance startProcessInstanceByKey(String processDefinitionKey, BusinessKey businessKey, Map<String, Object> variables) {
        return runtimeService.startProcessInstanceByKey(processDefinitionKey, String.valueOf(businessKey), variables);
    }

    @Override
    public void setVariables(String executionId, Map<String, Object> variables) {
        runtimeService.setVariables(executionId, variables);
    }

    @Override
    public <V> V getVariable(String executionId, String variableName, Class<V> variableClass) {
        return runtimeService.getVariable(executionId, variableName, variableClass);
    }

    @Override
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        runtimeService.deleteProcessInstance(processInstanceId,
                Optional.ofNullable(deleteReason).orElse("deleted"));
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }

    @Override
    public Task getCurrentTask(String processInstanceId) {
        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .singleResult();
    }

    @Override
    public Task getTask(String processInstanceId, String taskKey) {
        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey(taskKey)
                .singleResult();
    }

    @Override
    public List<Task> listCurrentTasks(String processInstanceId) {
        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .list();
    }

}
