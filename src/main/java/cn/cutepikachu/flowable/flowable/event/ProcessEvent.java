package cn.cutepikachu.flowable.flowable.event;

import java.io.Serializable;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 事件对象接口
 * @since 2025/9/5 09:15:04
 */
public interface ProcessEvent extends Serializable {

    String getProcessDefinitionKey();

}
