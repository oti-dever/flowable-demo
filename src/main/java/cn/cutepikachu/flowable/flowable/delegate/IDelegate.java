package cn.cutepikachu.flowable.flowable.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 委托接口
 * @since 2025/8/29 13:11:51
 */
public interface IDelegate extends JavaDelegate {

    Logger log = LoggerFactory.getLogger(IDelegate.class);

    @Transactional(rollbackFor = Exception.class)
    @Override
    default void execute(DelegateExecution execution) {
        onExecute(execution);
        notifyExecute(execution);
    }

    default void onExecute(DelegateExecution execution) {
    }

    default void notifyExecute(DelegateExecution execution) {
        log.info("委托执行: 执行ID={}, 流程实例ID={}, 流程定义ID={}, 当前活动ID={}",
                execution.getId(), execution.getProcessInstanceId(), execution.getProcessDefinitionId(), execution.getCurrentActivityId());
    }

}
