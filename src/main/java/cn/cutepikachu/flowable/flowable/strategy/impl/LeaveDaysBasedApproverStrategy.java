package cn.cutepikachu.flowable.flowable.strategy.impl;

import cn.cutepikachu.flowable.dao.DepartmentDAO;
import cn.cutepikachu.flowable.dao.EmployeeBaseInfoDAO;
import cn.cutepikachu.flowable.dao.LeaveRequestDAO;
import cn.cutepikachu.flowable.enums.ApproverSelectStrategyEnum;
import cn.cutepikachu.flowable.exception.BusinessException;
import cn.cutepikachu.flowable.flowable.strategy.IApproverSelectStrategy;
import cn.cutepikachu.flowable.model.entity.EmployeeBaseInfo;
import cn.cutepikachu.flowable.model.entity.LeaveRequest;
import jakarta.annotation.Resource;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.util.List;

import static cn.cutepikachu.flowable.constant.CompanyConstant.BOSS_EMPLOYEE_CODEE;
import static cn.cutepikachu.flowable.constant.FlowableConstant.BUSINESS_ID;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 基于请假天数的审批策略
 * @since 2025/8/30
 */
@Component
public class LeaveDaysBasedApproverStrategy implements IApproverSelectStrategy {

    @Resource
    private EmployeeBaseInfoDAO employeeBaseInfoDAO;

    @Resource
    private DepartmentDAO departmentDAO;

    @Resource
    private LeaveRequestDAO leaveRequestDAO;

    @Override
    public List<Long> selectApprovers(DelegateTask delegateTask, Long starter) {
        Long leaveId = delegateTask.getVariable(BUSINESS_ID, Long.class);
        LeaveRequest leaveRequest = leaveRequestDAO.getById(leaveId);
        if (leaveRequest == null) {
            throw new BusinessException("找不到请假信息");
        }
        int days = leaveRequest.getDurationDays();
        EmployeeBaseInfo emp = employeeBaseInfoDAO.getByEmpCode(starter);
        if (emp == null) {
            throw new BusinessException("申请人不存在");
        }
        if (days <= 2) {
            // 2天以内，部门领导审批
            Long deptLeaderEmpCode = departmentDAO.findDeptLeaderByDeptCode(emp.getDeptCode());
            return List.of(deptLeaderEmpCode);
        } else if (days <= 6) {
            // 2-6天，人事审批
            List<EmployeeBaseInfo> hrs = employeeBaseInfoDAO.lambdaQuery()
                    .eq(EmployeeBaseInfo::getDeptCode, 70020L)
                    .list();
            if (hrs == null || hrs.isEmpty()) {
                throw new BusinessException("未配置人事负责人");
            }
            return hrs.stream().map(EmployeeBaseInfo::getEmployeeCode).toList();
        } else {
            // 6天以上，老板审批
            return List.of(BOSS_EMPLOYEE_CODEE);
        }
    }

    @Override
    public ApproverSelectStrategyEnum getStrategyType() {
        return ApproverSelectStrategyEnum.LEAVE_DAYS_BASED;
    }

}
