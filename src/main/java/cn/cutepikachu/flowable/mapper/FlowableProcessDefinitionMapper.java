package cn.cutepikachu.flowable.mapper;

import cn.cutepikachu.flowable.model.entity.FlowableProcessDefinition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程定义表Mapper
 * @since 2025/09/01 10:45:13
 */
@Mapper
public interface FlowableProcessDefinitionMapper extends BaseMapper<FlowableProcessDefinition> {

    List<FlowableProcessDefinition> listLatestProcessDefinition();

}
