package cn.cutepikachu.flowable.service;

import cn.cutepikachu.flowable.model.vo.LeaveRequestVO;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 请假服务接口
 * @since 2025/8/26 17:15:00
 */
public interface LeaveRequestService {

    LeaveRequestVO getById(Long id);

}
