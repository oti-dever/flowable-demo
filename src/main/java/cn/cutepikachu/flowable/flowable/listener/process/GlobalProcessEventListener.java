package cn.cutepikachu.flowable.flowable.listener.process;

import cn.cutepikachu.flowable.dao.ProcessDAO;
import cn.cutepikachu.flowable.enums.ProcessStatusEnum;
import cn.cutepikachu.flowable.flowable.service.IFlowableService;
import cn.cutepikachu.flowable.flowable.listener.manager.ProcessEventListenerManager;
import jakarta.annotation.Resource;
import org.flowable.common.engine.api.delegate.event.*;
import org.flowable.engine.delegate.event.FlowableCancelledEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static cn.cutepikachu.flowable.constant.FlowableConstant.PROCESS_ID;
import static org.flowable.common.engine.api.delegate.event.FlowableEngineEventType.PROCESS_CANCELLED;
import static org.flowable.common.engine.api.delegate.event.FlowableEngineEventType.PROCESS_COMPLETED;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 全局流程事件监听器
 * @since 2025/9/2 13:13:40
 */
@Component
public class GlobalProcessEventListener implements FlowableEventListener {

    @Resource
    private ProcessDAO processDAO;

    @Lazy
    @Resource
    private IFlowableService flowableService;

    @Resource
    private ProcessEventListenerManager processEventListenerManager;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onEvent(FlowableEvent event) {
        FlowableEventType eventType = event.getType();
        if (eventType.equals(PROCESS_COMPLETED)) {
            FlowableEngineEntityEvent completedEvent = (FlowableEngineEntityEvent) event;
            this.onProcessCompleted(completedEvent);

            // 分发给特定流程事件监听器
            processEventListenerManager.handleProcessCompletedBusiness(completedEvent);
        } else if (eventType.equals(PROCESS_CANCELLED)) {
            FlowableCancelledEvent deletedEvent = (FlowableCancelledEvent) event;
            this.onProcessDeleted(deletedEvent);

            // 分发给特定流程事件监听器
            processEventListenerManager.handleProcessCancelledBusiness(deletedEvent);
        }
    }

    /**
     * 一个过程已经完成。在最后一个活动ACTIVITY_COMPLETED后分派。当进程达到进程实例没有任何转换的状态时，即为进程完成
     */
    private void onProcessCompleted(FlowableEngineEntityEvent event) {
        // 流程完成时的处理逻辑
        String executionId = event.getExecutionId();
        Long processId = flowableService.getVariable(executionId, PROCESS_ID, Long.class);
        processDAO.updateStatusById(processId, ProcessStatusEnum.PASSED.getCode());
        processDAO.clearCurAssignees(processId);
    }

    private void onProcessDeleted(FlowableCancelledEvent event) {
        // 流程删除时的处理逻辑
        String executionId = event.getExecutionId();
        Long processId = flowableService.getVariable(executionId, PROCESS_ID, Long.class);
        processDAO.clearCurAssignees(processId);
    }

    @Override
    public boolean isFailOnException() {
        // 如果监听器执行逻辑失败，是否影响引擎的主事务
        // false表示即使监听器抛出异常，主流程事务也会继续提交
        return true;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        // 此监听器是否对事务生命周期事件感兴趣
        return false;
    }

    @Override
    public String getOnTransaction() {
        // 监听器应在哪个事务状态下触发
        return null;
    }

}
