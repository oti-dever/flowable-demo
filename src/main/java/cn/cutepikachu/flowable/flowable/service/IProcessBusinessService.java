package cn.cutepikachu.flowable.flowable.service;

import cn.cutepikachu.flowable.flowable.event.impl.ProcessStartedEvent;
import cn.cutepikachu.flowable.flowable.event.impl.TaskApprovedEvent;
import cn.cutepikachu.flowable.model.dto.ProcessTaskApproveDTO;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程业务服务接口
 * @since 2025/9/6 11:31:13
 */
public interface IProcessBusinessService {

    /**
     * 获取流程业务定义Key
     */
    String getProcessDefinitionKey();

    /**
     * 获取流程业务数据
     */
    Object getByBusinessData(Long businessId);

    /**
     * 发起流程，业务处理
     */
    <T> ProcessStartedEvent startProcessBusiness(Long processId, T startDTO);

    /**
     * 撤销流程，业务处理
     */
    Boolean cancelProcessBusiness(Long businessId, String comment);

    /**
     * 废弃流程，业务处理
     */
    Boolean discardProcessBusiness(Long businessId, String comment);

    /**
     * 审批任务，业务处理
     */
    TaskApprovedEvent approveTaskBusiness(Long businessId, ProcessTaskApproveDTO dto);

}
