package cn.cutepikachu.flowable.config;

import cn.cutepikachu.flowable.dao.FlowableProcessDefinitionDAO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/8/26 16:36:33
 */
@Slf4j
@Configuration
public class FlowableConfig implements ApplicationRunner {

    @Lazy
    @Resource
    private RepositoryService repositoryService;

    @Resource
    private FlowableProcessDefinitionDAO flowableProcessDefinitionDAO;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void run(ApplicationArguments args) throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 匹配所有流程模型文件
        org.springframework.core.io.Resource[] resources = resolver.getResources("classpath:flowable/**/*.xml");
        // 部署流程模型文件
        for (org.springframework.core.io.Resource resource : resources) {
            repositoryService.createDeployment()
                    // 防止重复部署
                    .enableDuplicateFiltering()
                    .addInputStream(resource.getFilename(), resource.getInputStream())
                    .name(resource.getFilename())
                    .deploy();
        }
        // 存储流程定义信息
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .list();
        flowableProcessDefinitionDAO.saveProcessDefinitions(processDefinitions);
        log.info("已加载并部署流程模型文件，共 {} 个", processDefinitions.size());
    }

    @Bean
    public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> springProcessEngineConfigurationConfigurer(FlowableEventListener globalProcessEventListener) {
        return engineConfiguration -> {
            engineConfiguration.setAnnotationFontName("宋体");
            engineConfiguration.setActivityFontName("宋体");
            engineConfiguration.setLabelFontName("宋体");

            // 监听器设置
            List<FlowableEventListener> listeners =
                    engineConfiguration.getEventListeners();
            if (listeners == null) {
                listeners = new ArrayList<>();
            }
            // 添加自定义全局监听器
            listeners.add(globalProcessEventListener);
            engineConfiguration.setEventListeners(listeners);
        };
    }

}
