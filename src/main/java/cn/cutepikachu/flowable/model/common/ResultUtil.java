package cn.cutepikachu.flowable.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 通用响应包装
 * @since 2025/8/26 16:20:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultUtil<DataType> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private DataType data;

    // Success

    public static ResultUtil<String> success() {
        return new ResultUtil<>(HttpStatus.OK.value(), "success", null);
    }

    public static ResultUtil<Boolean> success(String message) {
        return new ResultUtil<>(HttpStatus.OK.value(), message, true);
    }

    public static <DT> ResultUtil<DT> success(DT data) {
        return new ResultUtil<>(HttpStatus.OK.value(), "success", data);
    }

    public static <DT> ResultUtil<DT> success(String message, DT data) {
        return new ResultUtil<>(HttpStatus.OK.value(), message, data);
    }

    // Error

    public static ResultUtil<String> error() {
        return new ResultUtil<>(HttpStatus.BAD_REQUEST.value(), "error", null);
    }

    public static <DT> ResultUtil<DT> error(DT data) {
        return new ResultUtil<>(HttpStatus.BAD_REQUEST.value(), "error", data);
    }

    public static <DT> ResultUtil<DT> error(Integer code, DT data) {
        return new ResultUtil<>(code, "error", data);
    }

    public static <DT> ResultUtil<DT> error(Integer code, String message) {
        return new ResultUtil<>(code, message, null);
    }

    public static <DT> ResultUtil<DT> error(String message, DT data) {
        return new ResultUtil<>(HttpStatus.BAD_REQUEST.value(), message, data);
    }

    public static <DT> ResultUtil<DT> error(Integer code, String message, DT data) {
        return new ResultUtil<>(code, message, data);
    }

}

