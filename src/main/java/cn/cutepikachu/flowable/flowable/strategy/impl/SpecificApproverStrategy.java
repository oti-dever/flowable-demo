package cn.cutepikachu.flowable.flowable.strategy.impl;

import cn.cutepikachu.flowable.enums.ApproverSelectStrategyEnum;
import cn.cutepikachu.flowable.flowable.strategy.IApproverSelectStrategy;
import cn.cutepikachu.flowable.util.ListOfTypeUtil;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static cn.cutepikachu.flowable.constant.FlowableConstant.SPECIFIC_APPROVERS;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 指定审批人策略
 * @since 2025/9/1 11:24:09
 */
@Component
public class SpecificApproverStrategy implements IApproverSelectStrategy {

    @Override
    public List<Long> selectApprovers(DelegateTask delegateTask, Long starter) {
        String taskKey = delegateTask.getTaskDefinitionKey();
        Map<?, ?> specificApproversMap = delegateTask.getVariable(SPECIFIC_APPROVERS, Map.class);
        return ListOfTypeUtil.parseLongList((String) specificApproversMap.get(taskKey));
    }

    @Override
    public ApproverSelectStrategyEnum getStrategyType() {
        return ApproverSelectStrategyEnum.SPECIFIC;
    }

}
