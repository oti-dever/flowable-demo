package cn.cutepikachu.flowable.util;

import cn.hutool.core.util.ObjUtil;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/8/28 15:11:29
 */
public class EqualUtil {

    public static boolean equalsAny(Object source, Object ...targets) {
        for (Object target : targets) {
            if (ObjUtil.equals(source, target)) {
                return true;
            }
        }
        return false;
    }

    public static boolean notEqualsAll(Object source, Object ...targets) {
        for (Object target : targets) {
            if (ObjUtil.equals(source, target)) {
                return false;
            }
        }
        return true;
    }

}
