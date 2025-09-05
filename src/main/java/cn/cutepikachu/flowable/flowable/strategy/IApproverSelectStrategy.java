package cn.cutepikachu.flowable.flowable.strategy;

import cn.cutepikachu.flowable.enums.ApproverSelectStrategyEnum;
import org.flowable.task.service.delegate.DelegateTask;

import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 审批人选择策略接口
 * @since 2025/8/30
 */
public interface IApproverSelectStrategy {

    /**
     * 选择审批人
     *
     * @param delegateTask 任务委托对象
     * @param starter      流程发起人
     * @return 审批人列表
     */
    List<Long> selectApprovers(DelegateTask delegateTask, Long starter);

    /**
     * 获取策略类型
     *
     * @return 策略类型
     */
    ApproverSelectStrategyEnum getStrategyType();

}
