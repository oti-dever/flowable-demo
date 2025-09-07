package cn.cutepikachu.flowable.flowable.service.impl;

import cn.cutepikachu.flowable.dao.EmployeeBaseInfoDAO;
import cn.cutepikachu.flowable.dao.ProcessDAO;
import cn.cutepikachu.flowable.enums.ProcessStatusEnum;
import cn.cutepikachu.flowable.enums.error.ProcessErrorEnum;
import cn.cutepikachu.flowable.exception.BusinessException;
import cn.cutepikachu.flowable.flowable.event.impl.ProcessStartedEvent;
import cn.cutepikachu.flowable.flowable.event.impl.TaskApprovedEvent;
import cn.cutepikachu.flowable.flowable.service.IFlowableService;
import cn.cutepikachu.flowable.flowable.service.IProcessBusinessService;
import cn.cutepikachu.flowable.flowable.service.IProcessService;
import cn.cutepikachu.flowable.flowable.service.manager.ProcessBusinessServiceManager;
import cn.cutepikachu.flowable.model.dto.*;
import cn.cutepikachu.flowable.model.entity.FlowableProcessDefinition;
import cn.cutepikachu.flowable.model.entity.Process;
import cn.cutepikachu.flowable.model.vo.ProcessVO;
import cn.cutepikachu.flowable.util.TaskAssigneeMapUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.cutepikachu.flowable.constant.FlowableConstant.*;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/1 17:50:47
 */
@Service
public class GeneralProcessService implements IProcessService {

    @Resource
    private ProcessBusinessServiceManager processBusinessServiceManager;

    @Resource
    private ProcessDAO processDAO;

    @Resource
    private IFlowableService flowableService;

    @Resource
    private EmployeeBaseInfoDAO employeeBaseInfoDAO;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean saveProcessDraft(ProcessDraftSaveDTO dto) {
        Long processId = dto.getId();
        String draftData = dto.getDraftData();
        Long processDefinitionId = dto.getProcessDefinitionId();
        String title = dto.getTitle();
        Process process;
        if (processId != null) {
            process = processDAO.getById(processId);
            if (process == null || !ProcessStatusEnum.DRAFT.getCode().equals(process.getProcessStatus())) {
                throw new BusinessException("流程不存在或状态不为草稿");
            }
            process.setProcessDefinitionId(processDefinitionId);
            process.setTitle(title);
            process.setDraftData(draftData);
        } else {
            process = Process.builder()
                    .processDefinitionId(processDefinitionId)
                    .title(title)
                    .draftData(draftData)
                    .processStatus(ProcessStatusEnum.DRAFT.getCode())
                    // TODO 通过用户上下文获取
                    .createByCode(dto.getCreateByCode())
                    .createByName(dto.getCreateByName())
                    .build();
        }
        processDAO.saveOrUpdate(process);

        return Boolean.TRUE;
    }

    @Override
    public ProcessVO getProcessDraft(Long processId) {
        Process process = processDAO.getById(processId);
        if (process == null || !ProcessStatusEnum.DRAFT.getCode().equals(process.getProcessStatus())) {
            throw new BusinessException("流程不存在或状态不为草稿，无法获取");
        }
        return this.toVO(process, null);
    }

    @Override
    public ProcessVO getProcess(Long processId) {
        Process process = processDAO.getById(processId);
        // 获取流程业务数据
        FlowableProcessDefinition processDefinition = flowableService.getProcessDefinition(process.getProcessDefinitionId());
        if (processDefinition == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_DEFINITION_NOT_FOUND);
        }
        IProcessBusinessService processBusinessService = processBusinessServiceManager
                .getProcessService(processDefinition.getProcessDefinitionKey());
        Object businessData = processBusinessService.getByBusinessData(process.getBusinessId());

        return this.toVO(process, businessData);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> Boolean startProcess(ProcessStartDTO<T> dto) {
        Long processDefinitionId = dto.getProcessDefinitionId();
        T data = dto.getData();
        String title = dto.getTitle();
        Long createByCode = dto.getCreateByCode();
        String createByName = dto.getCreateByName();

        // 获取流程定义
        FlowableProcessDefinition processDefinition = flowableService.getProcessDefinition(processDefinitionId);
        if (processDefinition == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_DEFINITION_NOT_FOUND);
        }

        Process process;
        if (dto.getProcessId() == null) {
            // 直接发起
            process = Process.builder()
                    .processDefinitionId(processDefinitionId)
                    .title(title)
                    .processStatus(ProcessStatusEnum.IN_PROGRESS.getCode())
                    .draftData(JSONUtil.toJsonStr(data))
                    // TODO 通过用户上下文获取
                    .createByCode(createByCode)
                    .createByName(createByName)
                    .build();
        } else {
            // 草稿发起
            process = processDAO.getById(dto.getProcessId());
            if (process == null || !ProcessStatusEnum.DRAFT.getCode().equals(process.getProcessStatus())) {
                throw new BusinessException("流程不存在或状态不为草稿，无法发起");
            }
            process.setProcessDefinitionId(processDefinitionId);
            process.setTitle(title);
            process.setDraftData(JSONUtil.toJsonStr(data));
            process.setProcessStatus(ProcessStatusEnum.IN_PROGRESS.getCode());
        }
        processDAO.saveOrUpdate(process);

        // 获取流程业务服务
        IProcessBusinessService processBusinessService = processBusinessServiceManager
                .getProcessService(processDefinition.getProcessDefinitionKey());
        // 执行发起前的业务处理
        ProcessStartedEvent businessEvent = processBusinessService.startProcessBusiness(process.getId(), data);

        // 获取发起流程所需参数及业务信息
        String processDefinitionKey = businessEvent.getProcessDefinitionKey();
        Long businessId = businessEvent.getBusinessId();
        Map<String, Object> variables = businessEvent.getVariables();

        variables.put(STARTER, createByCode);
        variables.put(PROCESS_ID, process.getId());
        variables.put(BUSINESS_ID, businessId);

        // 启动流程
        ProcessInstance pi = flowableService.startProcessInstanceByKey(processDefinitionKey, businessId, variables);
        String processInstanceId = pi.getId();

        // 更新流程信息
        process.setBusinessId(businessId);
        process.setProcessInstanceId(processInstanceId);
        processDAO.updateById(process);

        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean cancelProcess(Long processId, ProcessCancelDTO dto) {
        // 流程是否为进行中
        Process process = processDAO.getById(processId);
        if (process == null || !ProcessStatusEnum.IN_PROGRESS.getCode().equals(process.getProcessStatus())) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_CANNOT_CANCELLED);
        }
        // 只有发起人可以撤销
        Long createByCode = process.getCreateByCode();
        // TODO 通过用户上下文获取
        if (!createByCode.equals(dto.getApplicantEmpCode())) {
            throw new BusinessException(ProcessErrorEnum.NO_PERMISSION);
        }

        process.setProcessStatus(ProcessStatusEnum.CANCELED.getCode());
        processDAO.updateById(process);

        String comment = dto.getComment();
        // 删除流程实例
        flowableService.deleteProcessInstance(process.getProcessInstanceId(), comment);

        // 获取流程业务服务
        FlowableProcessDefinition processDefinition = flowableService.getProcessDefinition(process.getProcessDefinitionId());
        IProcessBusinessService processBusinessService = processBusinessServiceManager
                .getProcessService(processDefinition.getProcessDefinitionKey());
        // 执行业务撤销操作
        return processBusinessService.cancelProcessBusiness(process.getBusinessId(), comment);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean discardProcess(Long processId, ProcessDiscardDDTO dto) {
        Process process = processDAO.getById(processId);
        if (process == null || !ProcessStatusEnum.IN_PROGRESS.getCode().equals(process.getProcessStatus())) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_CANNOT_DISCARD);
        }
        process.setProcessStatus(ProcessStatusEnum.DISCARDED.getCode());
        processDAO.updateById(process);

        String comment = dto.getComment();
        // 删除流程实例
        flowableService.deleteProcessInstance(process.getProcessInstanceId(), comment);

        // 获取流程业务服务
        FlowableProcessDefinition processDefinition = flowableService.getProcessDefinition(process.getProcessDefinitionId());
        IProcessBusinessService processBusinessService = processBusinessServiceManager
                .getProcessService(processDefinition.getProcessDefinitionKey());
        // 执行业务废弃操作
        return processBusinessService.discardProcessBusiness(process.getBusinessId(), comment);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean approveTask(Long processId, ProcessTaskApproveDTO dto) {
        // 流程是否为进行中
        Process process = processDAO.getById(processId);
        if (process == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }
        if (!ProcessStatusEnum.IN_PROGRESS.getCode().equals(process.getProcessStatus())) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_CANNOT_APPROVED);
        }

        // 查找对应的任务
        Task task = flowableService.getTask(process.getProcessInstanceId(), dto.getApprovalTask());
        if (task == null) {
            throw new BusinessException(ProcessErrorEnum.TASK_NOT_FOUND);
        }

        String taskId = task.getId();
        String taskKey = task.getTaskDefinitionKey();

        // 获取流程业务服务
        FlowableProcessDefinition processDefinition = flowableService.getProcessDefinition(process.getProcessDefinitionId());
        IProcessBusinessService processBusinessService = processBusinessServiceManager
                .getProcessService(processDefinition.getProcessDefinitionKey());
        // 执行审批前的业务处理
        TaskApprovedEvent businessEvent = processBusinessService.approveTaskBusiness(process.getBusinessId(), dto);

        // 获取审批任务所需参数及业务信息
        Map<String, Object> variables = businessEvent.getVariables();

        // 设置流程变量
        flowableService.setVariables(task.getExecutionId(), variables);

        Map<String, List<Long>> curAssigneeCodes = getCurrentAssignee(processId);
        List<Long> taskAssignee = TaskAssigneeMapUtil.getTaskAssignee(curAssigneeCodes, taskKey);

        // TODO 通过用户上下文获取当前审批人工号，这里先用传参代替
        Long approverCode = dto.getApproverEmpCode();
        // 是否包含在审批人列表中
        if (!taskAssignee.isEmpty() && !taskAssignee.contains(approverCode)) {
            throw new BusinessException(ProcessErrorEnum.TASK_NOT_ASSIGNED_TO_USER);
        }

        // 审批是否通过
        if (!dto.getApproved()) {
            // 审批未通过
            // 结束流程
            flowableService.deleteProcessInstance(process.getProcessInstanceId(), dto.getComment());
            // 标记流程为未通过
            processDAO.updateStatusById(processId, ProcessStatusEnum.REJECTED.getCode());
            return Boolean.TRUE;
        }

        // 审批通过
        // 当前任务所有审批人中已有人通过，移出当前任务审批人列表
        TaskAssigneeMapUtil.removeTask(curAssigneeCodes, taskKey);
        setCurrentAssignee(processId, curAssigneeCodes);

        // 完成任务
        flowableService.completeTask(taskId);

        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setCurrentAssignee(Long processId, Map<String, List<Long>> assignee) {
        Process process = processDAO.getById(processId);
        if (process == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }
        process.setCurAssigneeCodes(assignee);
        process.setCurAssigneeNames(buildAssigneeNames(assignee));
        processDAO.updateById(process);
    }

    private Map<String, List<String>> buildAssigneeNames(Map<String, List<Long>> assigneeCodes) {
        Map<String, List<String>> assigneeNames = new HashMap<>();
        assigneeCodes.forEach((taskKey, codes) -> {
            List<String> names;
            if (codes.isEmpty()) {
                names = List.of();
            } else {
                names = employeeBaseInfoDAO.listNamesByCodes(codes);
            }
            assigneeNames.put(taskKey, names);
        });
        return assigneeNames;
    }

    @Override
    public Map<String, List<Long>> getCurrentAssignee(Long processId) {
        Process process = processDAO.getById(processId);
        if (process == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }
        return process.getCurAssigneeCodes();
    }

    private ProcessVO toVO(Process process, Object businessData) {
        ProcessVO processVO = BeanUtil.copyProperties(process, ProcessVO.class);
        processVO.setBusinessData(businessData);
        return processVO;
    }

}
