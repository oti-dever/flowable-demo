package cn.cutepikachu.flowable.exception;

import cn.cutepikachu.flowable.model.common.ResultUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description 全局异常处理
 * @since 2025/8/26 17:50:00
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResultUtil<Void> handleBiz(BusinessException e) {
        return ResultUtil.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResultUtil<Void> handleValid(Exception e) {
        String msg = e instanceof MethodArgumentNotValidException manve && manve.getBindingResult().getFieldError() != null
                ? manve.getBindingResult().getFieldError().getDefaultMessage()
                : e.getMessage();
        return ResultUtil.error(400, msg);
    }

    @ExceptionHandler({ConstraintViolationException.class, HttpMessageNotReadableException.class, IllegalArgumentException.class})
    public ResultUtil<Void> handleBadRequest(Exception e) {
        return ResultUtil.error(400, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultUtil<Void> handleUnknown(Exception e) {
        log.error("系统异常", e);
        return ResultUtil.error(500, e.getMessage());
    }

}

