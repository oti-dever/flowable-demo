package cn.cutepikachu.flowable.flowable.delegate;

import cn.cutepikachu.flowable.dao.ProcessDAO;
import cn.cutepikachu.flowable.enums.ProcessStatusEnum;
import cn.cutepikachu.flowable.flowable.service.IFlowableService;
import cn.cutepikachu.flowable.model.entity.Process;
import jakarta.annotation.Resource;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.context.annotation.Lazy;

import static cn.cutepikachu.flowable.constant.FlowableConstant.PROCESS_ID;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程超时处理抽象类
 * @since 2025/09/02 15:31:40
 */
public abstract class AbstractProcessExpireDelegate implements IDelegate {

    @Resource
    private ProcessDAO processDAO;

    @Lazy
    @Resource
    private IFlowableService flowableService;

    @Override
    public void onExecute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        flowableService.deleteProcessInstance(processInstanceId, ProcessStatusEnum.EXPIRED.getDesc());
        Long processId = execution.getVariable(PROCESS_ID, Long.class);
        Process process = processDAO.getById(processId);
        processDAO.updateStatusById(processId, ProcessStatusEnum.EXPIRED.getCode());

        // 执行额外业务操作
        this.executeBusiness(execution, process.getBusinessId());
    }

    public void executeBusiness(DelegateExecution execution, Long businessId) {
    }

}
