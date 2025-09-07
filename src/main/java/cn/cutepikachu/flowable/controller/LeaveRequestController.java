package cn.cutepikachu.flowable.controller;

import cn.cutepikachu.flowable.service.LeaveRequestService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 请假流程 REST 控制器
 * @since 2025/8/26 17:40:00
 */
@RestController
@RequestMapping("/leave-requests")
public class LeaveRequestController {

    @Resource
    private LeaveRequestService leaveRequestService;

}
