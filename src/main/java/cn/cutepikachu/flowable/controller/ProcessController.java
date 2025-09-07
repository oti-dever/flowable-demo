package cn.cutepikachu.flowable.controller;

import cn.cutepikachu.flowable.flowable.service.IProcessService;
import cn.cutepikachu.flowable.model.common.ResultUtil;
import cn.cutepikachu.flowable.model.dto.*;
import cn.cutepikachu.flowable.model.vo.ProcessVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/2 09:53:37
 */
@RestController
@RequestMapping("/process")
public class ProcessController {

    @Resource
    private IProcessService processService;

    @PostMapping("/draft")
    public ResultUtil<Boolean> saveProcessDraft(@RequestBody @Valid ProcessDraftSaveDTO dto) {
        return ResultUtil.success(processService.saveProcessDraft(dto));
    }

    @GetMapping("/draft/{processId}")
    public ResultUtil<ProcessVO> getProcessDraft(@PathVariable Long processId) {
        return ResultUtil.success(processService.getProcessDraft(processId));
    }

    @GetMapping("/{processId}")
    public ResultUtil<ProcessVO> getProcessDetail(@PathVariable Long processId) {
        return ResultUtil.success(processService.getProcess(processId));
    }

    @PostMapping("/create")
    public <T> ResultUtil<Boolean> startProcess(@RequestBody @Valid ProcessStartDTO<T> dto) {
        return ResultUtil.success(processService.startProcess(dto));
    }

    @PostMapping("/cancel/{processId}")
    public ResultUtil<Boolean> cancelProcess(@PathVariable Long processId,
                                             @RequestBody @Valid ProcessCancelDTO dto) {
        return ResultUtil.success(processService.cancelProcess(processId, dto));
    }

    @PostMapping("/discard/{processId}")
    public ResultUtil<Boolean> discardProcess(@PathVariable Long processId,
                                              @RequestBody @Valid ProcessDiscardDDTO dto) {
        return ResultUtil.success(processService.discardProcess(processId, dto));
    }

    @PostMapping("/approve/{processId}")
    public <T> ResultUtil<Boolean> approveTask(@PathVariable Long processId,
                                               @RequestBody @Valid ProcessTaskApproveDTO dto) {
        return ResultUtil.success(processService.approveTask(processId, dto));
    }

}
