package cn.cutepikachu.flowable.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 请假单视图对象
 * @since 2025/8/26 16:45:00
 */
@Data
public class LeaveRequestVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long applicantEmpCode;

    private String leaveType;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer durationDays;

    private String reason;

    private List<String> attachments;

    private List<Long> approverEmpCodes;

    private List<Long> ccEmpCodes;

    private String status;

}
