package cn.cutepikachu.flowable.controller;

import cn.cutepikachu.flowable.model.common.ResultUtil;
import cn.cutepikachu.flowable.model.dto.*;
import cn.cutepikachu.flowable.model.vo.LeaveRequestVO;
import cn.cutepikachu.flowable.service.impl.LeaveRequestServiceImpl;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 请假流程 REST 控制器
 * @since 2025/8/26 17:40:00
 */
@RestController
@RequestMapping("/leave-requests")
public class LeaveRequestController {

    @Resource
    private LeaveRequestServiceImpl leaveRequestService;

    @PostMapping
    public ResultUtil<Boolean> create(@RequestBody @Valid ProcessStartDTO<LeaveRequestCreateDTO> dto) {
        return ResultUtil.success(leaveRequestService.startProcess(dto));
    }

    @DeleteMapping("/cancel")
    public ResultUtil<Boolean> cancel(@RequestBody @Valid ProcessCancelDTO dto) {
        return ResultUtil.success(leaveRequestService.cancelProcess(dto));
    }

    @DeleteMapping("/discard")
    public ResultUtil<Boolean> discard(@RequestBody @Valid ProcessDiscardDDTO dto) {
        return ResultUtil.success(leaveRequestService.discardProcess(dto));
    }

    @PutMapping("/approval/{processId}")
    public ResultUtil<Boolean> approve(@PathVariable Long processId,
            @RequestBody @Valid LeaveRequestApproveDTO dto) {
        return ResultUtil.success(leaveRequestService.approveTask(processId, dto));
    }

    @GetMapping("/{id}")
    public ResultUtil<LeaveRequestVO> get(@PathVariable("id") Long id) {
        return ResultUtil.success(leaveRequestService.getById(id));
    }

}
