package cn.cutepikachu.flowable.service;

import cn.cutepikachu.flowable.model.dto.ExpenseReimbursementUpdateDTO;
import cn.cutepikachu.flowable.model.vo.ExpenseReimbursementVO;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 报销申请服务接口
 * @since 2025/8/28 11:58:00
 */
public interface ExpenseReimbursementService {

    /**
     * 更新报销申请
     */
    ExpenseReimbursementVO update(ExpenseReimbursementUpdateDTO dto);

    /**
     * 根据ID查询报销申请
     */
    ExpenseReimbursementVO getById(Long id);

}
