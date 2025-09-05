package cn.cutepikachu.flowable.dao;

import cn.cutepikachu.flowable.mapper.TestForkJoinMapper;
import cn.cutepikachu.flowable.model.entity.TestForkJoin;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 测试 ForkJoin 流程 DAO
 * @since 2025/09/03 09:54:31
 */
@Service
public class TestForkJoinDAO extends ServiceImpl<TestForkJoinMapper, TestForkJoin> {

    public boolean updateStatusById(Long id, String status) {
        return this.lambdaUpdate()
                .set(TestForkJoin::getStatus, status)
                .eq(TestForkJoin::getId, id)
                .update();
    }

}
