package cn.cutepikachu.flowable.mapper;

import cn.cutepikachu.flowable.model.entity.LeaveRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 请假申请 Mapper
 * @since 2025/8/26 16:12:00
 */
@Mapper
public interface LeaveRequestMapper extends BaseMapper<LeaveRequest> {
}

