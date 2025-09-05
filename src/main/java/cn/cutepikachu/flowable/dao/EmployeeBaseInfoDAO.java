package cn.cutepikachu.flowable.dao;

import cn.cutepikachu.flowable.mapper.EmployeeBaseInfoMapper;
import cn.cutepikachu.flowable.model.entity.EmployeeBaseInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/8/26 14:26:11
 */
@Service
public class EmployeeBaseInfoDAO extends ServiceImpl<EmployeeBaseInfoMapper, EmployeeBaseInfo> {

    public List<String> listNamesByCodes(List<Long> codes) {
        return lambdaQuery()
                .in(EmployeeBaseInfo::getEmployeeCode, codes)
                .select(EmployeeBaseInfo::getEmployeeName)
                .list()
                .stream()
                .map(EmployeeBaseInfo::getEmployeeName)
                .toList();
    }

    public EmployeeBaseInfo getByEmpCode(Long empCode) {
        return lambdaQuery()
                .eq(EmployeeBaseInfo::getEmployeeCode, empCode)
                .one();
    }

}
