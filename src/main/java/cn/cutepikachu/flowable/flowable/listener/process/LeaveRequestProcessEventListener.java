package cn.cutepikachu.flowable.flowable.listener.process;

import cn.cutepikachu.flowable.dao.LeaveRequestDAO;
import cn.cutepikachu.flowable.enums.LeaveRequestStatus;
import cn.cutepikachu.flowable.flowable.listener.AbstractProcessEventListener;
import cn.cutepikachu.flowable.flowable.service.IFlowableService;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static cn.cutepikachu.flowable.constant.FlowableConstant.LeaveRequest.APPROVED;
import static cn.cutepikachu.flowable.constant.FlowableConstant.LeaveRequest.PROC_DEF_KEY;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/6 17:07:37
 */
@Component
public class LeaveRequestProcessEventListener extends AbstractProcessEventListener {

    @Lazy
    @Getter
    @Resource
    private IFlowableService flowableService;

    @Resource
    private LeaveRequestDAO leaveRequestDAO;

    @Override
    public void onProcessCompletedBusiness(FlowableEngineEvent event, Long businessId) {
        leaveRequestDAO.updateStatusById(businessId, LeaveRequestStatus.PASSED.getCode());
    }

    @Override
    public void onProcessDeleteBusiness(FlowableEngineEvent event, Long businessId) {
        String executionId = event.getExecutionId();
        Boolean approved = flowableService.getVariable(executionId, APPROVED, Boolean.class);
        LeaveRequestStatus status = approved ?
                LeaveRequestStatus.PASSED : LeaveRequestStatus.REJECTED;
        leaveRequestDAO.updateStatusById(businessId, status.getCode());
    }

    @Override
    public String getProcessDefinitionKey() {
        return PROC_DEF_KEY;
    }

}
