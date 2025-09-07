package cn.cutepikachu.flowable.controller;

import cn.cutepikachu.flowable.service.ExpenseReimbursementService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 报销流程 REST 控制器
 * @since 2025/8/28 12:10:00
 */
@RestController
@RequestMapping("/expense-reimbursements")
public class ExpenseReimbursementController {

    @Resource
    private ExpenseReimbursementService expenseReimbursementService;

}
