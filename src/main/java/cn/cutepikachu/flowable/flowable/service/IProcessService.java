package cn.cutepikachu.flowable.flowable.service;

import cn.cutepikachu.flowable.model.dto.*;
import cn.cutepikachu.flowable.model.vo.ProcessVO;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程服务 草稿/发起/撤销/废弃/审批 通用接口
 * @since 2025/9/1 14:04:33
 */
public interface IProcessService {

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
     * 撤销流程
     */
    Boolean cancelProcess(Long processId, ProcessCancelDTO dto);

    /**
     * 废弃流程
     */
    Boolean discardProcess(Long processId, ProcessDiscardDDTO dto);

    /**
     * 审批任务
     */
    Boolean approveTask(Long processId, ProcessTaskApproveDTO dto);

    /**
     * 设置当前审批人
     */
    void setCurrentAssignee(Long processId, Map<String, List<Long>> assignee);

    /**
     * 获取当前审批人
     */
    Map<String, List<Long>> getCurrentAssignee(Long businessId);

}
