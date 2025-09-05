package cn.cutepikachu.flowable.dao;

import cn.cutepikachu.flowable.constant.CompanyConstant;
import cn.cutepikachu.flowable.mapper.DepartmentMapper;
import cn.cutepikachu.flowable.model.entity.Department;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/8/26 14:25:35
 */
@Service
public class DepartmentDAO extends ServiceImpl<DepartmentMapper, Department> {

    /**
     * 根据部门编码查找部门负责人编码
     * 如果当前部门没有负责人，则一级级向上查找上级部门的负责人
     * 如果所有上级部门都没有负责人，则返回 BOSS_EMPLOYEE_CODEE
     *
     * @param deptCode 部门编码
     * @return 部门负责人编码
     */
    public Long findDeptLeaderByDeptCode(String deptCode) {
        if (deptCode == null || deptCode.trim().isEmpty()) {
            return CompanyConstant.BOSS_EMPLOYEE_CODEE;
        }

        String currentDeptCode = deptCode;
        Set<String> visitedDeptCodes = new HashSet<>();

        while (currentDeptCode != null && !currentDeptCode.trim().isEmpty()) {
            if (visitedDeptCodes.contains(currentDeptCode)) {
                break;
            }
            visitedDeptCodes.add(currentDeptCode);

            // 查找当前部门
            Department department = lambdaQuery()
                    .eq(Department::getDeptCode, currentDeptCode)
                    .one();

            if (department == null) {
                break;
            }

            // 如果当前部门有负责人，直接返回
            if (department.getDeptLeaderEmpCode() != null) {
                return department.getDeptLeaderEmpCode();
            }

            // 如果当前部门没有负责人，继续查找上级部门
            currentDeptCode = department.getParentDeptCode();
        }

        // 所有上级部门都没有负责人，返回 BOSS_EMPLOYEE_CODEE
        return CompanyConstant.BOSS_EMPLOYEE_CODEE;
    }

}
