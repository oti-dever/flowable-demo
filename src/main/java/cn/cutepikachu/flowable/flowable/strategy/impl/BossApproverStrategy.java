package cn.cutepikachu.flowable.flowable.strategy.impl;

import cn.cutepikachu.flowable.constant.CompanyConstant;
import cn.cutepikachu.flowable.enums.ApproverSelectStrategyEnum;
import cn.cutepikachu.flowable.flowable.strategy.IApproverSelectStrategy;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 老板审批策略
 * @since 2025/8/30
 */
@Component
public class BossApproverStrategy implements IApproverSelectStrategy {

    @Override
    public List<Long> selectApprovers(DelegateTask delegateTask, Long starter) {
        return List.of(CompanyConstant.BOSS_EMPLOYEE_CODEE);
    }

    @Override
    public ApproverSelectStrategyEnum getStrategyType() {
        return ApproverSelectStrategyEnum.BOSS;
    }

}
