package cn.cutepikachu.flowable.flowable.service;

import cn.cutepikachu.flowable.dao.EmployeeBaseInfoDAO;
import cn.cutepikachu.flowable.dao.ProcessDAO;
import cn.cutepikachu.flowable.flowable.IFlowableService;
import cn.cutepikachu.flowable.flowable.event.impl.ProcessStartedEvent;
import cn.cutepikachu.flowable.flowable.event.impl.TaskApprovedEvent;
import cn.cutepikachu.flowable.model.dto.ProcessCancelDTO;
import cn.cutepikachu.flowable.model.dto.ProcessDiscardDDTO;
import cn.cutepikachu.flowable.model.dto.ProcessDraftSaveDTO;
import cn.cutepikachu.flowable.model.dto.ProcessStartDTO;
import cn.cutepikachu.flowable.model.vo.ProcessVO;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程服务 草稿/发起/撤销/废弃/审批 通用接口
 * @since 2025/9/1 14:04:33
 */
public interface IProcessService {

    ProcessDAO getProcessDAO();

    IFlowableService getFlowableService();

    EmployeeBaseInfoDAO getEmployeeBaseInfoDAO();

    /**
     * 创建流程草稿
     */
    Boolean saveProcessDraft(ProcessDraftSaveDTO dto);

    /**
     * 获取流程草稿数据
     */
    ProcessVO getProcessDraft(Long processId);

    /**
     * 获取流程信息
     */
    ProcessVO getProcess(Long processId);

    /**
     * 发起流程
     */
    <T> Boolean startProcess(ProcessStartDTO<T> dto);

    /**
     * 发起流程，业务处理
     */
    <T> ProcessStartedEvent startProcessBusiness(Long processId, T startDTO);

    /**
     * 撤销流程
     */
    Boolean cancelProcess(ProcessCancelDTO dto);

    /**
     * 撤销流程，业务处理
     */
    Boolean cancelProcessBusiness(Long businessId, String comment);

    /**
     * 废弃流程
     */
    Boolean discardProcess(ProcessDiscardDDTO dto);

    /**
     * 废弃流程，业务处理
     */
    Boolean discardProcessBusiness(Long businessId, String comment);

    /**
     * 审批任务
     */
    <T> Boolean approveTask(Long processId, T dto);

    /**
     * 审批任务，业务处理
     */
    <T> TaskApprovedEvent approveTaskBusiness(Long businessId, T approveDTO);

    /**
     * 设置当前审批人
     */
    void setCurrentAssignee(Long processId, Map<String, List<Long>> assignee);

    /**
     * 获取当前审批人
     */
    Map<String, List<Long>> getCurrentAssignee(Long businessId);

}
