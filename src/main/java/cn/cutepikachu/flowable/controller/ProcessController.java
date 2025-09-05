package cn.cutepikachu.flowable.controller;

import cn.cutepikachu.flowable.model.common.ResultUtil;
import cn.cutepikachu.flowable.model.dto.ProcessDraftSaveDTO;
import cn.cutepikachu.flowable.model.vo.ProcessVO;
import cn.cutepikachu.flowable.flowable.service.IProcessService;
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
    private IProcessService defaultProcessService;

    @PostMapping("/draft")
    public ResultUtil<Boolean> saveProcessDraft(@RequestBody @Valid ProcessDraftSaveDTO dto){
        return ResultUtil.success(defaultProcessService.saveProcessDraft(dto));
    }

    @GetMapping("/draft/{processId}")
    public ResultUtil<ProcessVO> getProcessDraft(@PathVariable Long processId){
        return ResultUtil.success(defaultProcessService.getProcessDraft(processId));
    }

    @GetMapping("/{processId}")
    public ResultUtil<ProcessVO> getProcessDetail(@PathVariable Long processId){
        return ResultUtil.success(defaultProcessService.getProcess(processId));
    }

}
