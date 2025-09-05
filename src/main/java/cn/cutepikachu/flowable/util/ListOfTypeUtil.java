package cn.cutepikachu.flowable.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/1 13:14:23
 */
public class ListOfTypeUtil {

    private static final String SEPARATOR = ",";
    private static final String EMPTY = "";

    public static String toString(List<?> list) {
        if (list == null || list.isEmpty()) {
            return EMPTY;
        }
        return list.stream().map(String::valueOf).collect(Collectors.joining(SEPARATOR));
    }

    public static List<Byte> parseByteList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(str.split(SEPARATOR)).map(Byte::valueOf).collect(Collectors.toList());
    }

    public static List<Short> parseShortList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(str.split(SEPARATOR)).map(Short::valueOf).collect(Collectors.toList());
    }

    public static List<Integer> parseIntegerList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(str.split(SEPARATOR)).map(Integer::valueOf).collect(Collectors.toList());
    }

    public static List<Long> parseLongList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(str.split(SEPARATOR)).map(Long::valueOf).collect(Collectors.toList());
    }

    public static List<Float> parseFloatList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(str.split(SEPARATOR)).map(Float::valueOf).collect(Collectors.toList());
    }

    public static List<Double> parseDoubleList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(str.split(SEPARATOR)).map(Double::valueOf).collect(Collectors.toList());
    }

    public static List<BigInteger> parseBigIntegerList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(str.split(SEPARATOR)).map(BigInteger::new).collect(Collectors.toList());
    }

    public static List<BigDecimal> parseBigDecimalList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(str.split(SEPARATOR)).map(BigDecimal::new).collect(Collectors.toList());
    }

    public static List<Boolean> parseBooleanList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(str.split(SEPARATOR)).map(Boolean::valueOf).collect(Collectors.toList());
    }

    public static List<Character> parseCharList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return str.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
    }

    public static List<String> parseStringList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(str.split(SEPARATOR));
    }

}
