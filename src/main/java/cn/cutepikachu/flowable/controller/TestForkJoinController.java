package cn.cutepikachu.flowable.controller;

import cn.cutepikachu.flowable.service.TestForkJoinService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 测试 ForkJoin 流程 REST 控制器
 * @since 2025/9/4 13:31:27
 */
@RestController
@RequestMapping("/test-fork-joins")
public class TestForkJoinController {

    @Resource
    private TestForkJoinService testForkJoinService;

}
