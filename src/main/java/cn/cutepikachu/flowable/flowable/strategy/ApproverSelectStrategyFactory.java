package cn.cutepikachu.flowable.flowable.strategy;

import cn.cutepikachu.flowable.enums.ApproverSelectStrategyEnum;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 审批人选择策略工厂
 * @since 2025/8/30
 */
@Component
public class ApproverSelectStrategyFactory {

    @Resource
    private List<IApproverSelectStrategy> strategies;

    private final Map<ApproverSelectStrategyEnum, IApproverSelectStrategy> strategyMap = new HashMap<>();

    @PostConstruct
    public void init() {
        for (IApproverSelectStrategy strategy : strategies) {
            strategyMap.put(strategy.getStrategyType(), strategy);
        }
    }

    /**
     * 根据策略枚举获取策略实例
     *
     * @param strategyEnum 策略枚举
     * @return 策略实例
     */
    public IApproverSelectStrategy getStrategy(ApproverSelectStrategyEnum strategyEnum) {
        IApproverSelectStrategy strategy = strategyMap.get(strategyEnum);
        if (strategy == null) {
            throw new IllegalArgumentException("找不到策略实现: " + strategyEnum.getCode());
        }
        return strategy;
    }

}
