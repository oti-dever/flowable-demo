package cn.cutepikachu.flowable.controller;

import cn.cutepikachu.flowable.model.common.ResultUtil;
import cn.cutepikachu.flowable.model.dto.ProcessCancelDTO;
import cn.cutepikachu.flowable.model.dto.ProcessDiscardDDTO;
import cn.cutepikachu.flowable.model.dto.ProcessStartDTO;
import cn.cutepikachu.flowable.model.dto.TestForkJoinApprovalDTO;
import cn.cutepikachu.flowable.model.dto.TestForkJoinCreateDTO;
import cn.cutepikachu.flowable.model.entity.TestForkJoin;
import cn.cutepikachu.flowable.service.impl.TestForkJoinServiceImpl;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 测试 ForkJoin 流程 REST 控制器
 * @since 2025/9/4 13:31:27
 */
@RestController
@RequestMapping("/test-fork-joins")
public class TestForkJoinController {

    @Resource
    private TestForkJoinServiceImpl testForkJoinService;

    @PostMapping
    public ResultUtil<Boolean> create(@RequestBody @Valid ProcessStartDTO<TestForkJoinCreateDTO> dto) {
        return ResultUtil.success(testForkJoinService.startProcess(dto));
    }

    @DeleteMapping("/cancel")
    public ResultUtil<Boolean> cancel(@RequestBody @Valid ProcessCancelDTO dto) {
        return ResultUtil.success(testForkJoinService.cancelProcess(dto));
    }

    @DeleteMapping("/discard")
    public ResultUtil<Boolean> discard(@RequestBody @Valid ProcessDiscardDDTO dto) {
        return ResultUtil.success(testForkJoinService.discardProcess(dto));
    }

    @PutMapping("/approval/{processId}")
    public ResultUtil<Boolean> approve(@PathVariable Long processId,
            @RequestBody @Valid TestForkJoinApprovalDTO dto) {
        return ResultUtil.success(testForkJoinService.approveTask(processId, dto));
    }

    @GetMapping("/{id}")
    public ResultUtil<TestForkJoin> get(@PathVariable("id") Long id) {
        return ResultUtil.success(testForkJoinService.getById(id));
    }

}
