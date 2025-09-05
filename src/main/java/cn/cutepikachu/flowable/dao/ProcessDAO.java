package cn.cutepikachu.flowable.dao;

import cn.cutepikachu.flowable.mapper.ProcessMapper;
import cn.cutepikachu.flowable.model.entity.Process;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程详情表DAO
 * @since 2025/09/01 14:01:02
 */
@Service
public class ProcessDAO extends ServiceImpl<ProcessMapper, Process> {

    public boolean clearCurAssignees(Long processId) {
        return this.lambdaUpdate()
                .set(Process::getCurAssigneeCodes, "{}")
                .set(Process::getCurAssigneeNames, "{}")
                .eq(Process::getId, processId)
                .update();
    }

    public Process getByBusinessId(Long businessId) {
        return this.lambdaQuery()
                .eq(Process::getBusinessId, businessId)
                .one();
    }

    public boolean updateStatusById(Long id, String status) {
        return this.lambdaUpdate()
                .set(Process::getProcessStatus, status)
                .eq(Process::getId, id)
                .update();
    }

}
