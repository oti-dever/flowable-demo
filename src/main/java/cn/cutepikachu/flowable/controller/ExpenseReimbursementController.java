package cn.cutepikachu.flowable.controller;

import cn.cutepikachu.flowable.model.common.ResultUtil;
import cn.cutepikachu.flowable.model.dto.*;
import cn.cutepikachu.flowable.model.vo.ExpenseReimbursementVO;
import cn.cutepikachu.flowable.service.impl.ExpenseReimbursementServiceImpl;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 报销流程 REST 控制器
 * @since 2025/8/28 12:10:00
 */
@RestController
@RequestMapping("/expense-reimbursements")
public class ExpenseReimbursementController {

    @Resource
    private ExpenseReimbursementServiceImpl expenseReimbursementService;

    @PostMapping
    public ResultUtil<Boolean> create(@RequestBody @Valid ProcessStartDTO<ExpenseReimbursementCreateDTO> dto) {
        return ResultUtil.success(expenseReimbursementService.startProcess(dto));
    }

    @DeleteMapping("/cancel")
    public ResultUtil<Boolean> cancel(@RequestBody @Valid ProcessCancelDTO dto) {
        return ResultUtil.success(expenseReimbursementService.cancelProcess(dto));
    }

    @DeleteMapping("/discard")
    public ResultUtil<Boolean> discard(@RequestBody @Valid ProcessDiscardDDTO dto) {
        return ResultUtil.success(expenseReimbursementService.discardProcess(dto));
    }

    @PutMapping
    public ResultUtil<ExpenseReimbursementVO> update(@RequestBody @Valid ExpenseReimbursementUpdateDTO dto) {
        return ResultUtil.success(expenseReimbursementService.update(dto));
    }

    @PutMapping("/approval/{processId}")
    public ResultUtil<Boolean> approve(@PathVariable Long processId,
            @RequestBody @Valid ExpenseReimbursementApprovalDTO dto) {
        return ResultUtil.success(expenseReimbursementService.approveTask(processId, dto));
    }

    @GetMapping("/{id}")
    public ResultUtil<ExpenseReimbursementVO> get(@PathVariable("id") Long id) {
        return ResultUtil.success(expenseReimbursementService.getById(id));
    }

}
