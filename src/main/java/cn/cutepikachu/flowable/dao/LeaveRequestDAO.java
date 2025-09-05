package cn.cutepikachu.flowable.dao;

import cn.cutepikachu.flowable.mapper.LeaveRequestMapper;
import cn.cutepikachu.flowable.model.entity.LeaveRequest;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 请假申请 DAO
 * @since 2025/8/26 16:15:00
 */
@Service
public class LeaveRequestDAO extends ServiceImpl<LeaveRequestMapper, LeaveRequest> {

    public boolean updateApproverById(Long id, String approvers) {
        return lambdaUpdate()
                .eq(LeaveRequest::getId, id)
                .set(LeaveRequest::getApproverEmpCodes, approvers)
                .update();
    }

    public boolean updateStatusById(Long id, String status) {
        return lambdaUpdate()
                .eq(LeaveRequest::getId, id)
                .set(LeaveRequest::getStatus, status)
                .update();
    }

}
