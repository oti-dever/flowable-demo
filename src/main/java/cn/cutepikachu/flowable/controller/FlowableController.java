package cn.cutepikachu.flowable.controller;

import cn.cutepikachu.flowable.model.vo.ProcessDefinitionVO;
import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description Flowable 流程相关给你控制器
 * @since 2025/8/28 11:16:17
 */
@RestController
@RequestMapping("/flowable")
public class FlowableController {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private HistoryService historyService;

    @Resource
    private RuntimeService runtimeService;

    private static final ProcessDiagramGenerator PROCESS_DIAGRAM_GENERATOR = new DefaultProcessDiagramGenerator();

    @GetMapping("/list/process-definitions")
    public List<ProcessDefinitionVO> listProcessDefinitions() {
        return repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .list()
                .stream()
                .map(pd -> BeanUtil.copyProperties(pd, ProcessDefinitionVO.class))
                .toList();
    }

    @GetMapping("/diagram/deployed/{processDefinitionId}")
    public void exportDeployedDiagram(@PathVariable String processDefinitionId, HttpServletResponse response) {
        // RepositoryService.getProcessDiagram() 方法会智能地找到并返回部署的图表资源
        try (InputStream diagramStream = repositoryService.getProcessDiagram(processDefinitionId)) {

            if (diagramStream == null) {
                // 您可以在这里抛出异常或进行其他错误处理
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Diagram not found for process definition: " + processDefinitionId);
                return;
            }

            // 从流程定义信息中获取图片资源名称，以确定Content-Type
            String diagramResourceName = repositoryService.getProcessDefinition(processDefinitionId).getDiagramResourceName();
            if (diagramResourceName.endsWith(".png")) {
                response.setContentType("image/png");
            } else if (diagramResourceName.endsWith(".svg")) {
                response.setContentType("image/svg+xml");
            } else {
                response.setContentType("application/octet-stream");
            }

            // 使用try-with-resources确保输出流被正确关闭
            try (OutputStream outputStream = response.getOutputStream()) {
                diagramStream.transferTo(outputStream);
                outputStream.flush();
            }

        } catch (Exception e) {
            // 处理异常
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/diagram/process/{processInstanceId}")
    public void getProcessDiagram(@PathVariable String processInstanceId, HttpServletResponse response) throws IOException {
        boolean isHistory = false;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        String processDefinitionId = null;
        if (processInstance != null) {
            processDefinitionId = processInstance.getProcessDefinitionId();
        } else {
            isHistory = true;
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            if (historicProcessInstance != null) {
                processDefinitionId = historicProcessInstance.getProcessDefinitionId();
            }
        }

        if (processDefinitionId == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Process instance not found");
            return;
        }

        // 获取BPMN模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        // 获取需要高亮的活动节点
        List<String> highLightedActivities = runtimeService.createActivityInstanceQuery()
                .processInstanceId(processInstanceId).list().stream()
                .map(ActivityInstance::getActivityId)
                .toList();
        List<String> highLightedActivitiesHistory = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).list().stream()
                .map(HistoricActivityInstance::getActivityId)
                .toList();

        // 获取需要高亮的流程线 (这里需要更复杂的逻辑，此处仅为示例)
        // ...

        // 2. 调用接口中的方法生成图片流
        // 3. 将生成的图片流输出给前端
        try (InputStream imageStream = PROCESS_DIAGRAM_GENERATOR.generateDiagram(
                bpmnModel,
                // 图片类型
                "png",
                // 高亮节点
                highLightedActivities,
                // 高亮连线
                Collections.emptyList(),
                // 节点字体，防止中文乱码
                "宋体",
                // 标签字体
                "宋体",
                // 注解字体
                "宋体",
                // 使用默认类加载器
                null,
                // 默认缩放比例
                1.0,
                // 绘制连线名称
                true
        ); OutputStream os = response.getOutputStream()) {
            response.setContentType("image/png");
            imageStream.transferTo(os);
            os.flush();
        }

    }

}
