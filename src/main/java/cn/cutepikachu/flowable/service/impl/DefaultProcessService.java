package cn.cutepikachu.flowable.service.impl;

import cn.cutepikachu.flowable.dao.EmployeeBaseInfoDAO;
import cn.cutepikachu.flowable.dao.ProcessDAO;
import cn.cutepikachu.flowable.flowable.IFlowableService;
import cn.cutepikachu.flowable.flowable.event.impl.ProcessStartedEvent;
import cn.cutepikachu.flowable.flowable.event.impl.TaskApprovedEvent;
import cn.cutepikachu.flowable.flowable.service.AbstractProcessService;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/1 17:50:47
 */
@Service
public class DefaultProcessService extends AbstractProcessService {

    @Getter
    @Resource
    private ProcessDAO processDAO;

    @Getter
    @Resource
    private IFlowableService flowableService;

    @Getter
    @Resource
    private EmployeeBaseInfoDAO employeeBaseInfoDAO;

    @Override
    public ProcessStartedEvent startProcessBusiness(Long processId, Object dto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Boolean cancelProcessBusiness(Long businessId, String comment) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Boolean discardProcessBusiness(Long businessId, String comment) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public <T> TaskApprovedEvent approveTaskBusiness(Long businessId, T approveDTO) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
