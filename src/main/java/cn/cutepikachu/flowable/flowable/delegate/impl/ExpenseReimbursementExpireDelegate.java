package cn.cutepikachu.flowable.flowable.delegate.impl;

import cn.cutepikachu.flowable.dao.ExpenseReimbursementDAO;
import cn.cutepikachu.flowable.enums.ExpenseReimbursementStatus;
import cn.cutepikachu.flowable.flowable.delegate.AbstractProcessExpireDelegate;
import jakarta.annotation.Resource;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 报销流程超时清理处理委托
 * @since 2025/8/28 12:15:00
 */
@Component
public class ExpenseReimbursementExpireDelegate extends AbstractProcessExpireDelegate {

    @Resource
    private ExpenseReimbursementDAO expenseReimbursementDAO;

    @Override
    public void executeBusiness(DelegateExecution execution, Long businessId) {
        // 更新报销申请状态为已过期
        expenseReimbursementDAO.updateStatusById(businessId, ExpenseReimbursementStatus.EXPIRED.getCode());
    }

}
