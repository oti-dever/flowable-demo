package cn.cutepikachu.flowable.enums.error;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/4 10:40:21
 */
public interface ErrorEnum {

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    Integer getErrorCode();

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    String getErrorMsg();

}
