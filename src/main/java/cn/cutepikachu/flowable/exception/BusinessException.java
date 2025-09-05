package cn.cutepikachu.flowable.exception;

import cn.cutepikachu.flowable.enums.error.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 业务异常
 * @since 2025/8/26 16:55:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected Integer code;

    /**
     * 错误信息
     */
    protected String message;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ErrorEnum error) {
        super(error.getErrorMsg());
        this.code = error.getErrorCode();
        this.message = error.getErrorMsg();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}

