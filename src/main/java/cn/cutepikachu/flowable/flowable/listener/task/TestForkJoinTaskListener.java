package cn.cutepikachu.flowable.flowable.listener.task;

import cn.cutepikachu.flowable.dao.TestForkJoinDAO;
import cn.cutepikachu.flowable.enums.TestForkJoinStatusEnum;
import cn.cutepikachu.flowable.flowable.listener.AbstractTaskListener;
import cn.cutepikachu.flowable.flowable.strategy.ApproverSelectStrategyFactory;
import cn.cutepikachu.flowable.flowable.service.IProcessService;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import static cn.cutepikachu.flowable.constant.FlowableConstant.BUSINESS_ID;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/4 14:23:34
 */
@Component
public class TestForkJoinTaskListener extends AbstractTaskListener {

    @Getter
    @Resource
    private ApproverSelectStrategyFactory approverSelectStrategyFactory;

    @Getter
    @Resource
    private IProcessService defaultProcessService;

    @Resource
    private TestForkJoinDAO testForkJoinDAO;

    @Override
    public void onCreate(DelegateTask delegateTask) {
        Long testId = delegateTask.getVariable(BUSINESS_ID, Long.class);
        testForkJoinDAO.updateStatusById(testId, TestForkJoinStatusEnum.IN_PROGRESS.getCode());
    }

}
