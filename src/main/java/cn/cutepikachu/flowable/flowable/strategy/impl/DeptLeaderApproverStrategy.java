package cn.cutepikachu.flowable.flowable.strategy.impl;

import cn.cutepikachu.flowable.dao.DepartmentDAO;
import cn.cutepikachu.flowable.dao.EmployeeBaseInfoDAO;
import cn.cutepikachu.flowable.enums.ApproverSelectStrategyEnum;
import cn.cutepikachu.flowable.flowable.strategy.IApproverSelectStrategy;
import cn.cutepikachu.flowable.model.entity.EmployeeBaseInfo;
import jakarta.annotation.Resource;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 部门领导审批策略
 * @since 2025/8/30
 */
@Component
public class DeptLeaderApproverStrategy implements IApproverSelectStrategy {

    @Resource
    private EmployeeBaseInfoDAO employeeBaseInfoDAO;

    @Resource
    private DepartmentDAO departmentDAO;

    @Override
    public List<Long> selectApprovers(DelegateTask delegateTask, Long starter) {
        EmployeeBaseInfo emp = employeeBaseInfoDAO.getByEmpCode(starter);
        if (emp == null) {
            throw new RuntimeException("找不到员工信息，员工编号：" + starter);
        }
        String deptCode = emp.getDeptCode();
        Long deptLeaderCode = departmentDAO.findDeptLeaderByDeptCode(deptCode);
        return List.of(deptLeaderCode);
    }

    @Override
    public ApproverSelectStrategyEnum getStrategyType() {
        return ApproverSelectStrategyEnum.DEPT_LEADER;
    }

}
