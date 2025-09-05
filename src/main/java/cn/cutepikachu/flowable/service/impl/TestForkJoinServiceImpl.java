package cn.cutepikachu.flowable.service.impl;

import cn.cutepikachu.flowable.dao.EmployeeBaseInfoDAO;
import cn.cutepikachu.flowable.dao.ProcessDAO;
import cn.cutepikachu.flowable.dao.TestForkJoinDAO;
import cn.cutepikachu.flowable.enums.TestForkJoinStatusEnum;
import cn.cutepikachu.flowable.enums.error.ProcessErrorEnum;
import cn.cutepikachu.flowable.exception.BusinessException;
import cn.cutepikachu.flowable.flowable.IFlowableService;
import cn.cutepikachu.flowable.flowable.event.impl.ProcessStartedEvent;
import cn.cutepikachu.flowable.flowable.event.impl.TaskApprovedEvent;
import cn.cutepikachu.flowable.flowable.service.AbstractProcessService;
import cn.cutepikachu.flowable.model.dto.TestForkJoinApprovalDTO;
import cn.cutepikachu.flowable.model.dto.TestForkJoinCreateDTO;
import cn.cutepikachu.flowable.model.entity.Process;
import cn.cutepikachu.flowable.model.entity.TestForkJoin;
import cn.cutepikachu.flowable.service.TestForkJoinService;
import cn.cutepikachu.flowable.util.ListOfTypeUtil;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.util.*;

import static cn.cutepikachu.flowable.constant.FlowableConstant.*;
import static cn.cutepikachu.flowable.constant.FlowableConstant.TestForkJoin.*;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/3 16:47:34
 */
@Service
public class TestForkJoinServiceImpl
        extends AbstractProcessService
        implements TestForkJoinService {

    @Getter
    @Resource
    private ProcessDAO processDAO;

    @Getter
    @Resource
    private IFlowableService flowableService;

    @Getter
    @Resource
    private EmployeeBaseInfoDAO employeeBaseInfoDAO;

    @Resource
    private TestForkJoinDAO testForkJoinDAO;

    @Override
    public <T> ProcessStartedEvent startProcessBusiness(Long processId, T startDTO) {
        TestForkJoinCreateDTO dto;
        try {
            dto = (TestForkJoinCreateDTO) startDTO;
        } catch (ClassCastException e) {
            throw new BusinessException("参数类型错误");
        }
        Long applicantEmpCode = dto.getApplicantEmpCode();
        List<Long> task1Assignees = dto.getTask1Assignees();
        List<Long> task2Assignees = dto.getTask2Assignees();
        List<Long> task3Assignees = dto.getTask3Assignees();
        List<Long> task4Assignees = dto.getTask4Assignees();

        TestForkJoin entity = new TestForkJoin();
        entity.setApplicantEmpCode(applicantEmpCode);
        entity.setTask1Assignees(task1Assignees);
        entity.setTask2Assignees(task2Assignees);
        entity.setTask3Assignees(task3Assignees);
        entity.setTask4Assignees(task4Assignees);

        boolean saved = testForkJoinDAO.save(entity);
        if (!saved) {
            throw new BusinessException("测试流程保存失败");
        }
        Long businessId = entity.getId();

        // 构建流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(STARTER, dto.getApplicantEmpCode());
        variables.put(SPECIFIC_APPROVERS, Map.of(
                TASK_1, ListOfTypeUtil.toString(task1Assignees),
                TASK_2, ListOfTypeUtil.toString(task2Assignees),
                TASK_3, ListOfTypeUtil.toString(task3Assignees),
                TASK_4, ListOfTypeUtil.toString(task4Assignees)
        ));
        variables.put(ASSIGNEE_STRATEGY, ASSIGNEE_STRATEGY_MAP);

        return new ProcessStartedEvent(
                PROC_DEF_KEY,
                businessId,
                variables
        );
    }

    @Override
    public Boolean cancelProcessBusiness(Long businessId, String comment) {
        TestForkJoin fk = testForkJoinDAO.getById(businessId);
        if (fk == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }
        // TODO 用户上下文获取申请人工号
        // if (!Objects.equals(fk.getApplicantEmpCode(), dto.getApplicantEmpCode())) {
        //     throw new BusinessException(ProcessErrorEnum.NO_PERMISSION);
        // }
        testForkJoinDAO.updateStatusById(businessId, TestForkJoinStatusEnum.CANCELED.getCode());
        return Boolean.TRUE;
    }

    @Override
    public Boolean discardProcessBusiness(Long businessId, String comment) {
        TestForkJoin fk = testForkJoinDAO.getById(businessId);
        if (fk == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }
        if (!Objects.equals(fk.getStatus(), TestForkJoinStatusEnum.IN_PROGRESS.getCode())) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_CANNOT_DISCARD);
        }
        testForkJoinDAO.updateStatusById(businessId, TestForkJoinStatusEnum.DISCARDED.getCode());
        return Boolean.TRUE;
    }

    @Override
    public <T> TaskApprovedEvent approveTaskBusiness(Long businessId, T approveDTO) {
        TestForkJoinApprovalDTO dto;
        try {
            dto = (TestForkJoinApprovalDTO) approveDTO;
        } catch (ClassCastException e) {
            throw new BusinessException("参数类型错误");
        }
        TestForkJoin fk = testForkJoinDAO.getById(businessId);
        if (fk == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }
        if (!Objects.equals(fk.getStatus(), TestForkJoinStatusEnum.IN_PROGRESS.getCode())) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_CANNOT_APPROVED);
        }

        // 查找对应的任务
        Process process = processDAO.getByBusinessId(fk.getId());
        Task task = flowableService.getTask(process.getProcessInstanceId(), dto.getApprovalTask());
        if (task == null) {
            throw new BusinessException(ProcessErrorEnum.TASK_NOT_FOUND);
        }

        // 完成任务
        Map<String, Object> variables = Collections.emptyMap();

        return new TaskApprovedEvent(
                PROC_DEF_KEY,
                variables,
                task.getTaskDefinitionKey(),
                task.getId(),
                dto.getApproverEmpCode()
        );
    }

    @Override
    public TestForkJoin getById(Long id) {
        return testForkJoinDAO.getById(id);
    }

}
