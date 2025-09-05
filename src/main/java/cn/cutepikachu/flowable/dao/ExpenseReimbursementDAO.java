package cn.cutepikachu.flowable.dao;

import cn.cutepikachu.flowable.mapper.ExpenseReimbursementMapper;
import cn.cutepikachu.flowable.model.entity.ExpenseReimbursement;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 报销申请 DAO
 * @since 2025/8/28 11:42:00
 */
@Service
public class ExpenseReimbursementDAO extends ServiceImpl<ExpenseReimbursementMapper, ExpenseReimbursement> {

    public boolean updateStatusById(Long id, String status) {
        return lambdaUpdate()
                .eq(ExpenseReimbursement::getId, id)
                .set(ExpenseReimbursement::getStatus, status)
                .update();
    }

    public boolean updateDeptLeaderById(Long id, String deptLeaderEmpCode) {
        return lambdaUpdate()
                .eq(ExpenseReimbursement::getId, id)
                .set(ExpenseReimbursement::getDeptLeaderEmpCode, deptLeaderEmpCode)
                .update();
    }

    public boolean updateBossById(Long id, String bossEmpCode) {
        return lambdaUpdate()
                .eq(ExpenseReimbursement::getId, id)
                .set(ExpenseReimbursement::getBossEmpCode, bossEmpCode)
                .update();
    }

    public boolean updateFinanceById(Long id, String financeEmpCodes) {
        return lambdaUpdate()
                .eq(ExpenseReimbursement::getId, id)
                .set(ExpenseReimbursement::getFinanceEmpCodes, financeEmpCodes)
                .update();
    }

    public boolean updateApprovalCommentById(Long id, String deptComment, String bossComment, String financeComment) {
        return lambdaUpdate()
                .eq(ExpenseReimbursement::getId, id)
                .set(ExpenseReimbursement::getDeptApprovalComment, deptComment)
                .set(ExpenseReimbursement::getBossApprovalComment, bossComment)
                .set(ExpenseReimbursement::getFinanceApprovalComment, financeComment)
                .update();
    }

    public boolean updateApprovalTimeById(Long id, LocalDateTime deptApprovalTime, LocalDateTime bossApprovalTime, LocalDateTime financeApprovalTime) {
        return lambdaUpdate()
                .eq(ExpenseReimbursement::getId, id)
                .set(ExpenseReimbursement::getDeptApprovalTime, deptApprovalTime)
                .set(ExpenseReimbursement::getBossApprovalTime, bossApprovalTime)
                .set(ExpenseReimbursement::getFinanceApprovalTime, financeApprovalTime)
                .update();
    }

}
