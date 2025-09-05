package cn.cutepikachu.flowable.flowable.listener;

import cn.cutepikachu.flowable.enums.ApproverSelectStrategyEnum;
import cn.cutepikachu.flowable.flowable.strategy.IApproverSelectStrategy;
import cn.cutepikachu.flowable.flowable.service.IProcessService;
import cn.cutepikachu.flowable.util.ListOfTypeUtil;
import cn.cutepikachu.flowable.util.TaskAssigneeMapUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.service.delegate.DelegateTask;

import java.util.List;
import java.util.Map;

import static cn.cutepikachu.flowable.constant.FlowableConstant.*;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 任务监听器抽象类
 * @since 2025/8/27 15:31:53
 */
@Slf4j
public abstract class AbstractTaskListener implements ITaskListener {

    @Override
    public final void notify(DelegateTask delegateTask) {
        String event = delegateTask.getEventName();
        switch (event) {
            case EVENTNAME_CREATE -> {
                // 任务创建时，设置任务审批人
                addAssignee(delegateTask);

                onCreate(delegateTask);
                notifyCreate(delegateTask);
            }
            case EVENTNAME_ASSIGNMENT -> {
                onAssignment(delegateTask);
                notifyAssignment(delegateTask);
            }
            case EVENTNAME_COMPLETE -> {
                // 任务完成时，移除任务审批人
                removeAssignee(delegateTask);

                onComplete(delegateTask);
                notifyComplete(delegateTask);
            }
            case EVENTNAME_DELETE -> {
                onDelete(delegateTask);
                notifyDelete(delegateTask);
            }
            case EVENTNAME_ALL_EVENTS -> {
                onAllEvents(delegateTask);
                notifyAllEvents(delegateTask);
            }
            default -> {
            }
        }
    }

    protected final void addAssignee(DelegateTask delegateTask) {
        // 根绝策略选择审批人
        Map<?, ?> assigneeStrategyMap = delegateTask.getVariable(ASSIGNEE_STRATEGY, Map.class);
        String taskKey = delegateTask.getTaskDefinitionKey();
        ApproverSelectStrategyEnum strategy = (ApproverSelectStrategyEnum) assigneeStrategyMap.get(taskKey);
        IApproverSelectStrategy selectStrategy = getApproverSelectStrategyFactory().getStrategy(strategy);
        Long starter = delegateTask.getVariable(STARTER, Long.class);
        List<Long> approvers = selectStrategy.selectApprovers(delegateTask, starter);

        IProcessService defaultProcessService = getDefaultProcessService();
        // 设置任务审批人
        delegateTask.setAssignee(ListOfTypeUtil.toString(approvers));
        Long processId = delegateTask.getVariable(PROCESS_ID, Long.class);
        Map<String, List<Long>> assignee = defaultProcessService.getCurrentAssignee(processId);
        if (assignee == null) {
            assignee = TaskAssigneeMapUtil.createTaskAssigneeMap();
        }
        TaskAssigneeMapUtil.setTaskAssignee(assignee, taskKey, approvers);
        defaultProcessService.setCurrentAssignee(processId, assignee);
    }

    protected final void removeAssignee(DelegateTask delegateTask) {
        IProcessService defaultProcessService = getDefaultProcessService();
        // 移除任务审批人
        Long processId = delegateTask.getVariable(PROCESS_ID, Long.class);
        Map<String, List<Long>> assignee = defaultProcessService.getCurrentAssignee(processId);
        String taskKey = delegateTask.getTaskDefinitionKey();
        TaskAssigneeMapUtil.removeTask(assignee, taskKey);
        defaultProcessService.setCurrentAssignee(processId, assignee);
    }

    @Override
    public void onCreate(DelegateTask delegateTask) {
    }

    @Override
    public void onAssignment(DelegateTask delegateTask) {
    }

    @Override
    public void onDelete(DelegateTask delegateTask) {
    }

    @Override
    public void onComplete(DelegateTask delegateTask) {
    }

    @Override
    public void onAllEvents(DelegateTask delegateTask) {
    }

    @Override
    public void notifyCreate(DelegateTask task) {
        log.info("任务创建: 任务ID={}, 任务名称={}, 任务定义Key={}, 流程实例ID={}",
                task.getId(), task.getName(), task.getTaskDefinitionKey(), task.getProcessInstanceId());
    }

    @Override
    public void notifyAssignment(DelegateTask task) {
        log.info("任务分配: 任务ID={}, 任务名称={}, 任务定义Key={}, 流程实例ID={}, 受理人={}",
                task.getId(), task.getName(), task.getTaskDefinitionKey(), task.getProcessInstanceId(), task.getAssignee());
    }

    @Override
    public void notifyComplete(DelegateTask task) {
        log.info("任务完成: 任务ID={}, 任务名称={}, 任务定义Key={}, 流程实例ID={}",
                task.getId(), task.getName(), task.getTaskDefinitionKey(), task.getProcessInstanceId());
    }

    @Override
    public void notifyDelete(DelegateTask task) {
        log.info("任务删除: 任务ID={}, 任务名称={}, 任务定义Key={}, 流程实例ID={}",
                task.getId(), task.getName(), task.getTaskDefinitionKey(), task.getProcessInstanceId());
    }

    @Override
    public void notifyAllEvents(DelegateTask task) {
    }

}
