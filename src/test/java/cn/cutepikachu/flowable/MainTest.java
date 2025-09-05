package cn.cutepikachu.flowable;

import jakarta.annotation.Resource;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/8/22 15:33:01
 */
@SpringBootTest
public class MainTest {

    public static void main(String[] args) {
        Duration duration = Duration.ofDays(7).plusSeconds(10);
        System.out.println(duration);
    }

    @Resource
    private ProcessEngine processEngine;

    @Resource
    private RepositoryService repositoryService;

    @Test
    public void test() {
        System.out.println(repositoryService != null && repositoryService == processEngine.getRepositoryService());
    }

    /**
     * 部署流程
     */
    @Test
    public void testDeploy() {
        Deployment deploy = repositoryService
                // 创建一个部署
                .createDeployment()
                // 部署的流程图
                .addClasspathResource("flowable/")
                // 执行部署
                .deploy();
        System.out.println(deploy);
    }


    @Resource
    private RuntimeService runtimeService;

    /**
     * 启动流程
     * <p>
     * ID 或 Key 从 ACT_RE_PROCDEF 表获取（ID_和KEY_）
     */
    @Test
    public void testStart() {
        // 传递表达式变量
        // 值表达式 ${assignee}
        // 方法表达式 ${userService.findAssigneeByTaskId(taskId)}，调用 userService（Bean） 动态获取
        Map<String, Object> variables = Map.of("assignee", "pikachu");
        ProcessInstance processInstanceById = runtimeService.startProcessInstanceById("oneTaskProcess:1:a1d5da31-821b-11f0-a0d0-50e97100f562", variables);
        // ProcessInstance processInstanceByKey = runtimeService.startProcessInstanceByKey("oneTaskProcess");
        System.out.println(processInstanceById);
        // System.out.println(processInstanceByKey);
        // System.out.println(ObjUtil.equals(processInstanceById, processInstanceByKey));
    }

    /**
     * 挂起流程实例
     */
    @Test
    public void testSuspendTask() {
        runtimeService.suspendProcessInstanceById("a4ec7a3c-8224-11f0-b7af-50e97100f562");
    }

    /**
     * 激活流程实例
     */
    @Test
    public void testActivateTask() {
        runtimeService.activateProcessInstanceById("a4f5a204-8224-11f0-b7af-50e97100f562");
    }

    @Test
    public void query() {
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
                .list();
        for (ProcessInstance processInstance : processInstances) {
            runtimeService.deleteProcessInstance(processInstance.getProcessInstanceId(), "delete");
        }
    }


    @Resource
    private TaskService taskService;

    /**
     * 查询某个用户的代办任务
     */
    @Test
    public void testQuery() {
        List<Task> taskList = taskService
                .createTaskQuery()
                .taskAssignee("pikachu")
                .list();
        System.out.println(taskList);
    }


}
