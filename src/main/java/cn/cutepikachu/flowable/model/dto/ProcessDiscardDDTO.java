package cn.cutepikachu.flowable.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 流程废弃 DTO
 * @since 2025/9/2 09:36:07
 */
@Data
public class ProcessDiscardDDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Size(max = 256)
    private String comment;

}
