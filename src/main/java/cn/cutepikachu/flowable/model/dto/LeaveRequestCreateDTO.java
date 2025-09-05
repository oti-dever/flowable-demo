package cn.cutepikachu.flowable.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 请假创建请求DTO
 * @since 2025/8/26 16:30:00
 */
@Data
public class LeaveRequestCreateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * TODO 通过用户上下文获取
     * 申请人工号
     */
    @NotNull(message = "申请人工号不能为空")
    private Long applicantEmpCode;

    @NotBlank(message = "请假类型不能为空")
    private String leaveType;

    @NotNull(message = "开始时间不能为空")
    private String startTime;

    @NotNull(message = "结束时间不能为空")
    private String endTime;

    @Size(max = 1024)
    private String reason;

    /**
     * 附件文件 ID 列表
     */
    private List<String> attachments;

    /**
     * 可选：前端指定审批人工号，如不传后端将根据规则自动挑选
     */
    private List<Long> approverEmpCode;

    /**
     * 抄送人列表（员工工号）
     */
    private List<Long> ccEmpCodes;

}

