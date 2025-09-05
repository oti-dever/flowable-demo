package cn.cutepikachu.flowable.flowable.delegate.impl;

import cn.cutepikachu.flowable.dao.LeaveRequestDAO;
import cn.cutepikachu.flowable.enums.LeaveRequestStatus;
import cn.cutepikachu.flowable.flowable.delegate.AbstractProcessExpireDelegate;
import jakarta.annotation.Resource;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 请假流程超时清理处理委托
 * @since 2025/9/2 16:13:30
 */
@Component
public class LeaveRequestExpireDelegate extends AbstractProcessExpireDelegate {

    @Resource
    private LeaveRequestDAO leaveRequestDAO;

    @Override
    protected void executeBusiness(DelegateExecution execution, Long businessId) {
        // 更新请假申请状态为已过期
        leaveRequestDAO.updateStatusById(businessId, LeaveRequestStatus.EXPIRED.getCode());
    }

}
