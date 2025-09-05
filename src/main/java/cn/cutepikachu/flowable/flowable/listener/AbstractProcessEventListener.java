package cn.cutepikachu.flowable.flowable.listener;

import cn.cutepikachu.flowable.flowable.IFlowableService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;
import org.springframework.context.annotation.Lazy;

import static cn.cutepikachu.flowable.constant.FlowableConstant.BUSINESS_ID;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 特定流程事件监听器抽象基类，提供通用功能和模板方法
 * @since 2025/9/3 17:17:15
 */
@Slf4j
public abstract class AbstractProcessEventListener implements IProcessEventListener {

    @Lazy
    @Resource
    protected IFlowableService flowableService;

    /**
     * 流程完成事件处理模板方法
     *
     * @param event 流程完成事件
     */
    @Override
    public final void onProcessCompletedBusiness(FlowableEngineEvent event) {
        try {
            // 获取业务ID
            Long businessId = getBusinessId(event);
            // 执行具体的业务处理
            onProcessCompletedBusiness(event, businessId);
            log.info("流程完成事件处理成功: 流程定义Key={}, 流程实例ID={}, 业务ID={}",
                    getProcessDefinitionKey(), event.getProcessInstanceId(), businessId);
        } catch (Exception e) {
            log.error("处理流程完成事件失败: 流程定义Key={}, 流程实例ID={}",
                    getProcessDefinitionKey(), event.getProcessInstanceId(), e);
            throw e;
        }
    }

    /**
     * 处理流程完成的具体业务逻辑
     * 子类必须实现此方法
     *
     * @param event      流程完成事件
     * @param businessId 业务ID
     */
    protected void onProcessCompletedBusiness(FlowableEngineEvent event, Long businessId) {
    }

    /**
     * 流程删除事件处理模板方法
     *
     * @param event 流程删除事件
     */
    @Override
    public final void onProcessDeleteBusiness(FlowableEngineEvent event) {
        try {
            // 获取业务ID
            Long businessId = getBusinessId(event);
            // 执行具体的业务处理
            onProcessDeleteBusiness(event, businessId);
            log.info("流程删除事件处理成功: 流程定义Key={}, 流程实例ID={}, 业务ID={}",
                    getProcessDefinitionKey(), event.getProcessInstanceId(), businessId);
        } catch (Exception e) {
            log.error("处理流程删除事件失败: 流程定义Key={}, 流程实例ID={}",
                    getProcessDefinitionKey(), event.getProcessInstanceId(), e);
            throw e;
        }
    }

    /**
     * 处理流程删除的具体业务逻辑
     * 子类必须实现此方法
     *
     * @param event      流程删除事件
     * @param businessId 业务ID
     */
    protected void onProcessDeleteBusiness(FlowableEngineEvent event, Long businessId) {
    }

    /**
     * 获取业务ID
     * 默认实现：从流程变量中获取PROCESS_ID
     * 子类可以重写此方法以获取不同的业务ID
     *
     * @param event 流程事件
     * @return 业务ID
     */
    protected final Long getBusinessId(FlowableEngineEvent event) {
        String executionId = event.getExecutionId();
        return flowableService.getVariable(executionId, BUSINESS_ID, Long.class);
    }

}
