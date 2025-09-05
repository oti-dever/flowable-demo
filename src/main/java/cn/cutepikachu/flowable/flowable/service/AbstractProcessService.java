package cn.cutepikachu.flowable.flowable.service;

import cn.cutepikachu.flowable.dao.EmployeeBaseInfoDAO;
import cn.cutepikachu.flowable.dao.ProcessDAO;
import cn.cutepikachu.flowable.enums.ProcessStatusEnum;
import cn.cutepikachu.flowable.enums.error.ProcessErrorEnum;
import cn.cutepikachu.flowable.exception.BusinessException;
import cn.cutepikachu.flowable.flowable.IFlowableService;
import cn.cutepikachu.flowable.flowable.event.impl.ProcessStartedEvent;
import cn.cutepikachu.flowable.flowable.event.impl.TaskApprovedEvent;
import cn.cutepikachu.flowable.model.dto.ProcessCancelDTO;
import cn.cutepikachu.flowable.model.dto.ProcessDiscardDDTO;
import cn.cutepikachu.flowable.model.dto.ProcessDraftSaveDTO;
import cn.cutepikachu.flowable.model.dto.ProcessStartDTO;
import cn.cutepikachu.flowable.model.entity.FlowableProcessDefinition;
import cn.cutepikachu.flowable.model.entity.Process;
import cn.cutepikachu.flowable.model.vo.ProcessVO;
import cn.cutepikachu.flowable.util.TaskAssigneeMapUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.cutepikachu.flowable.constant.FlowableConstant.*;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程服务抽象类
 * @since 2025/9/4 11:25:04
 */
public abstract class AbstractProcessService implements IProcessService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean saveProcessDraft(ProcessDraftSaveDTO dto) {
        ProcessDAO processDAO = getProcessDAO();
        Long processId = dto.getId();
        Process process;
        if (processId != null) {
            process = processDAO.getById(processId);
            if (process == null || !ProcessStatusEnum.DRAFT.getCode().equals(process.getProcessStatus())) {
                throw new BusinessException("流程不存在或状态不为草稿");
            }
            process.setProcessDefinitionId(dto.getProcessDefinitionId());
            process.setTitle(dto.getTitle());
            process.setData(dto.getData());
        } else {
            process = Process.builder()
                    .processDefinitionId(dto.getProcessDefinitionId())
                    .title(dto.getTitle())
                    .data(dto.getData())
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
        Process process = getProcessDAO().getById(processId);
        if (process == null || !ProcessStatusEnum.DRAFT.getCode().equals(process.getProcessStatus())) {
            throw new BusinessException("流程不存在或状态不为草稿，无法获取");
        }
        return this.toVO(process);
    }

    @Override
    public ProcessVO getProcess(Long processId) {
        Process process = getProcessDAO().getById(processId);
        return this.toVO(process);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> Boolean startProcess(ProcessStartDTO<T> dto) {
        IFlowableService flowableService = getFlowableService();
        Long processDefinitionId = dto.getProcessDefinitionId();
        FlowableProcessDefinition processDefinition = flowableService.getProcessDefinition(processDefinitionId);
        if (processDefinition == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_DEFINITION_NOT_FOUND);
        }
        ProcessDAO processDAO = getProcessDAO();
        Process process;
        if (dto.getProcessId() == null) {
            // 直接发起
            process = Process.builder()
                    .processDefinitionId(processDefinitionId)
                    .title(dto.getTitle())
                    .processStatus(ProcessStatusEnum.IN_PROGRESS.getCode())
                    .data(JSONUtil.toJsonStr(dto.getData()))
                    // TODO 通过用户上下文获取
                    .createByCode(dto.getCreateByCode())
                    .createByName(dto.getCreateByName())
                    .build();
        } else {
            // 草稿发起
            process = processDAO.getById(dto.getProcessId());
            if (process == null || !ProcessStatusEnum.DRAFT.getCode().equals(process.getProcessStatus())) {
                throw new BusinessException("流程不存在或状态不为草稿，无法发起");
            }
            process.setProcessDefinitionId(processDefinitionId);
            process.setTitle(dto.getTitle());
            process.setData(JSONUtil.toJsonStr(dto.getData()));
            process.setProcessStatus(ProcessStatusEnum.IN_PROGRESS.getCode());
        }
        processDAO.saveOrUpdate(process);

        // 执行发起前的业务处理
        ProcessStartedEvent businessEvent = this.startProcessBusiness(process.getId(), dto.getData());

        // 获取发起流程所需参数及业务信息
        String processDefinitionKey = businessEvent.getProcessDefinitionKey();
        Long businessId = businessEvent.getBusinessId();
        Map<String, Object> variables = businessEvent.getVariables();

        variables.put(STARTER, dto.getCreateByCode());
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

    @Override
    public abstract <T> ProcessStartedEvent startProcessBusiness(Long processId, T startDTO);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean cancelProcess(ProcessCancelDTO dto) {
        ProcessDAO processDAO = getProcessDAO();
        Process process = processDAO.getById(dto.getProcessId());
        if (process == null || !ProcessStatusEnum.IN_PROGRESS.getCode().equals(process.getProcessStatus())) {
            throw new BusinessException("流程不存在或状态不为进行中，无法撤销");
        }
        process.setProcessStatus(ProcessStatusEnum.CANCELED.getCode());
        processDAO.updateById(process);

        String comment = dto.getComment();
        // 删除流程实例
        getFlowableService().deleteProcessInstance(process.getProcessInstanceId(), comment);

        // 执行业务撤销操作
        return this.cancelProcessBusiness(process.getBusinessId(), comment);
    }

    @Override
    public abstract Boolean cancelProcessBusiness(Long businessId, String comment);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean discardProcess(ProcessDiscardDDTO dto) {
        ProcessDAO processDAO = getProcessDAO();
        Process process = processDAO.getById(dto.getProcessId());
        if (process == null || ProcessStatusEnum.DISCARDED.getCode().equals(process.getProcessStatus())) {
            throw new BusinessException("流程不存在或已被废弃，无法废弃");
        }
        process.setProcessStatus(ProcessStatusEnum.DISCARDED.getCode());
        processDAO.updateById(process);

        String comment = dto.getComment();
        // 删除流程实例
        getFlowableService().deleteProcessInstance(process.getProcessInstanceId(), comment);

        // 执行业务废弃操作
        return this.discardProcessBusiness(process.getBusinessId(), comment);
    }

    @Override
    public abstract Boolean discardProcessBusiness(Long businessId, String comment);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> Boolean approveTask(Long processId, T dto) {
        ProcessDAO processDAO = getProcessDAO();
        Process process = processDAO.getById(processId);
        if (process == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }

        // 执行审批前的业务处理
        TaskApprovedEvent businessEvent = this.approveTaskBusiness(process.getBusinessId(), dto);

        // 获取审批任务所需参数及业务信息
        String taskId = businessEvent.getTaskId();
        String taskKey = businessEvent.getTaskDefinitionKey();
        Map<String, Object> variables = businessEvent.getVariables();
        // TODO 通过用户上下文获取当前审批人工号，这里先用传参代替
        Long approverCode = businessEvent.getApproverEmpCode();

        Map<String, List<Long>> curAssigneeCodes = getCurrentAssignee(processId);
        List<Long> taskAssignee = TaskAssigneeMapUtil.getTaskAssignee(curAssigneeCodes, taskKey);

        // 是否包含在审批人列表中
        if (!taskAssignee.contains(approverCode)) {
            throw new BusinessException(ProcessErrorEnum.TASK_NOT_ASSIGNED_TO_USER);
        }

        // 将当前审批人移出审批人列表
        TaskAssigneeMapUtil.removeAssignee(curAssigneeCodes, taskKey, approverCode);
        setCurrentAssignee(processId, curAssigneeCodes);

        // 当前任务是否已被所有审批人审批完
        if (taskAssignee.isEmpty()) {
            // 完成任务
            getFlowableService().completeTask(taskId, variables);
        }

        return Boolean.TRUE;
    }

    @Override
    public abstract <T> TaskApprovedEvent approveTaskBusiness(Long businessId, T approveDTO);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setCurrentAssignee(Long processId, Map<String, List<Long>> assignee) {
        ProcessDAO processDAO = getProcessDAO();
        Process process = processDAO.getById(processId);
        if (process == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }
        process.setCurAssigneeCodes(assignee);
        process.setCurAssigneeNames(buildAssigneeNames(assignee));
        processDAO.updateById(process);
    }

    private Map<String, List<String>> buildAssigneeNames(Map<String, List<Long>> assigneeCodes) {
        EmployeeBaseInfoDAO employeeBaseInfoDAO = getEmployeeBaseInfoDAO();
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
    public final Map<String, List<Long>> getCurrentAssignee(Long processId) {
        Process process = getProcessDAO().getById(processId);
        if (process == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }
        return process.getCurAssigneeCodes();
    }

    public final ProcessVO toVO(Process process) {
        return BeanUtil.copyProperties(process, ProcessVO.class);
    }

}
