package cn.cutepikachu.flowable.flowable.listener.process;

import cn.cutepikachu.flowable.dao.TestForkJoinDAO;
import cn.cutepikachu.flowable.enums.TestForkJoinStatusEnum;
import cn.cutepikachu.flowable.flowable.listener.AbstractProcessEventListener;
import jakarta.annotation.Resource;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;
import org.springframework.stereotype.Component;

import static cn.cutepikachu.flowable.constant.FlowableConstant.TestForkJoin.PROC_DEF_KEY;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/4 09:49:43
 */
@Component
public class TestForkJoinProcessEventListener extends AbstractProcessEventListener {

    @Resource
    private TestForkJoinDAO testForkJoinDAO;

    @Override
    protected void onProcessCompletedBusiness(FlowableEngineEvent event, Long businessId) {
        testForkJoinDAO.updateStatusById(businessId, TestForkJoinStatusEnum.COMPLETED.getCode());
    }

    @Override
    public String getProcessDefinitionKey() {
        return PROC_DEF_KEY;
    }

}
