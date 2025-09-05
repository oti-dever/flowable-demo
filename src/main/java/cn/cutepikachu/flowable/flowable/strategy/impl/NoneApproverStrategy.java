package cn.cutepikachu.flowable.flowable.strategy.impl;

import cn.cutepikachu.flowable.enums.ApproverSelectStrategyEnum;
import cn.cutepikachu.flowable.flowable.strategy.IApproverSelectStrategy;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 无审批人策略
 * @since 2025/8/30
 */
@Component
public class NoneApproverStrategy implements IApproverSelectStrategy {

    @Override
    public List<Long> selectApprovers(DelegateTask delegateTask, Long starter) {
        return Collections.emptyList();
    }

    @Override
    public ApproverSelectStrategyEnum getStrategyType() {
        return ApproverSelectStrategyEnum.NONE;
    }

}
