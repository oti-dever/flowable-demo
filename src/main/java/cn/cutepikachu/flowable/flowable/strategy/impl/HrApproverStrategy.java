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
 * @description HR审批策略
 * @since 2025/8/30
 */
@Component
public class HrApproverStrategy implements IApproverSelectStrategy {

    @Resource
    private EmployeeBaseInfoDAO employeeBaseInfoDAO;

    @Override
    public List<Long> selectApprovers(DelegateTask delegateTask, Long starter) {
        List<EmployeeBaseInfo> hrs = employeeBaseInfoDAO.lambdaQuery()
                .eq(EmployeeBaseInfo::getDeptCode, 70020L)
                .list();
        if (hrs == null || hrs.isEmpty()) {
            throw new BusinessException("未配置人事负责人");
        }
        return hrs.stream().map(EmployeeBaseInfo::getEmployeeCode).toList();
    }

    @Override
    public ApproverSelectStrategyEnum getStrategyType() {
        return ApproverSelectStrategyEnum.HR;
    }

}
