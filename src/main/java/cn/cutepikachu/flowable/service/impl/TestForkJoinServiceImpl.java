package cn.cutepikachu.flowable.service.impl;

import cn.cutepikachu.flowable.dao.TestForkJoinDAO;
import cn.cutepikachu.flowable.enums.TestForkJoinStatusEnum;
import cn.cutepikachu.flowable.enums.error.ProcessErrorEnum;
import cn.cutepikachu.flowable.exception.BusinessException;
import cn.cutepikachu.flowable.flowable.event.impl.ProcessStartedEvent;
import cn.cutepikachu.flowable.flowable.event.impl.TaskApprovedEvent;
import cn.cutepikachu.flowable.model.dto.ProcessTaskApproveDTO;
import cn.cutepikachu.flowable.model.dto.TestForkJoinCreateDTO;
import cn.cutepikachu.flowable.model.entity.TestForkJoin;
import cn.cutepikachu.flowable.service.TestForkJoinService;
import cn.cutepikachu.flowable.util.ListOfTypeUtil;
import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.cutepikachu.flowable.constant.FlowableConstant.*;
import static cn.cutepikachu.flowable.constant.FlowableConstant.TestForkJoin.*;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/3 16:47:34
 */
@Service
public class TestForkJoinServiceImpl implements TestForkJoinService {

    @Resource
    private TestForkJoinDAO testForkJoinDAO;

    @Override
    public String getProcessDefinitionKey() {
        return PROC_DEF_KEY;
    }

    @Override
    public Object getByBusinessData(Long businessId) {
        TestForkJoin test = testForkJoinDAO.getById(businessId);
        if (test == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }
        return test;
    }

    @Override
    public <T> ProcessStartedEvent startProcessBusiness(Long processId, T startDTO) {
        TestForkJoinCreateDTO dto;
        try {
            dto = BeanUtil.toBean(startDTO, TestForkJoinCreateDTO.class);
        } catch (Exception e) {
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

        testForkJoinDAO.updateStatusById(businessId, TestForkJoinStatusEnum.CANCELED.getCode());
        return Boolean.TRUE;
    }

    @Override
    public Boolean discardProcessBusiness(Long businessId, String comment) {
        TestForkJoin fk = testForkJoinDAO.getById(businessId);
        if (fk == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }

        testForkJoinDAO.updateStatusById(businessId, TestForkJoinStatusEnum.DISCARDED.getCode());
        return Boolean.TRUE;
    }

    @Override
    public TaskApprovedEvent approveTaskBusiness(Long businessId, ProcessTaskApproveDTO dto) {
        TestForkJoin fk = testForkJoinDAO.getById(businessId);
        if (fk == null) {
            throw new BusinessException(ProcessErrorEnum.PROCESS_NOT_FOUND);
        }

        // 构建流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(APPROVED, dto.getApproved());

        return new TaskApprovedEvent(
                PROC_DEF_KEY,
                variables
        );
    }

}
