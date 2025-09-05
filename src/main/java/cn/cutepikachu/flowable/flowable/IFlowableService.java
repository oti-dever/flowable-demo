package cn.cutepikachu.flowable.flowable;

import cn.cutepikachu.flowable.model.entity.FlowableProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description Flowable流程引擎服务接口
 * @since 2025/9/1 09:34:25
 */
public interface IFlowableService {

    FlowableProcessDefinition getProcessDefinition(Long processDefinitionId);

    <BusinessKey> ProcessInstance startProcessInstanceByKey(String processDefinitionKey, BusinessKey businessKey, Map<String, Object> variables);

    void setVariables(String executionId, Map<String, Object> variables);

    <V> V getVariable(String executionId, String variableName, Class<V> variableClass);

    void deleteProcessInstance(String processInstanceId, String deleteReason);

    default void completeTask(String taskId) {
        completeTask(taskId, null);
    };

    void completeTask(String taskId, Map<String, Object> variables);

    Task getCurrentTask(String processInstanceId);

    Task getTask(String processInstanceId, String taskKey);

    List<Task> listCurrentTasks(String processInstanceId);

}
