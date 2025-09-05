package cn.cutepikachu.flowable.flowable.strategy.impl;

import cn.cutepikachu.flowable.dao.EmployeeBaseInfoDAO;
import cn.cutepikachu.flowable.enums.ApproverSelectStrategyEnum;
import cn.cutepikachu.flowable.exception.BusinessException;
import cn.cutepikachu.flowable.flowable.strategy.IApproverSelectStrategy;
import cn.cutepikachu.flowable.model.entity.EmployeeBaseInfo;
import jakarta.annotation.Resource;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 财务审批策略
 * @since 2025/8/30
 */
@Component
public class FinanceApproverStrategy implements IApproverSelectStrategy {

    @Resource
    private EmployeeBaseInfoDAO employeeBaseInfoDAO;

    @Override
    public List<Long> selectApprovers(DelegateTask delegateTask, Long starter) {
        List<EmployeeBaseInfo> finances = employeeBaseInfoDAO.lambdaQuery()
                .eq(EmployeeBaseInfo::getDeptCode, 70021L)
                .list();
        if (finances == null || finances.isEmpty()) {
            throw new BusinessException("未配置财务负责人");
        }
        return finances.stream().map(EmployeeBaseInfo::getEmployeeCode).toList();
    }

    @Override
    public ApproverSelectStrategyEnum getStrategyType() {
        return ApproverSelectStrategyEnum.FINANCE;
    }

}
