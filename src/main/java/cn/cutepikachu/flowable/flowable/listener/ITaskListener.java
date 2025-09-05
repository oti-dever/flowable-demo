package cn.cutepikachu.flowable.flowable.listener;

import cn.cutepikachu.flowable.flowable.strategy.ApproverSelectStrategyFactory;
import cn.cutepikachu.flowable.flowable.service.IProcessService;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 用户任务（审批）
 * @since 2025/9/4 14:33:42
 */
public interface ITaskListener extends TaskListener {

    ApproverSelectStrategyFactory getApproverSelectStrategyFactory();

    IProcessService getDefaultProcessService();

    /**
     * 任务创建
     *
     * @param delegateTask 任务
     */
    void onCreate(DelegateTask delegateTask);

    /**
     * 任务分配
     *
     * @param delegateTask 任务
     */
    void onAssignment(DelegateTask delegateTask);

    /**
     * 任务删除
     *
     * @param delegateTask 任务
     */
    void onDelete(DelegateTask delegateTask);

    /**
     * 任务完成
     *
     * @param delegateTask 任务
     */
    void onComplete(DelegateTask delegateTask);

    /**
     * 所有事件
     *
     * @param delegateTask 任务
     */
    void onAllEvents(DelegateTask delegateTask);


    void notifyCreate(DelegateTask task);

    void notifyAssignment(DelegateTask task);

    void notifyComplete(DelegateTask task);

    void notifyDelete(DelegateTask task);

    void notifyAllEvents(DelegateTask task);

}
