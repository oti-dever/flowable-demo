package cn.cutepikachu.flowable.typehander;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description Map&lt;String, List&lt;Long&gt;&gt;类型的TypeHandler，用于在JSON字符串和 Map&lt;String, List&lt;Long&gt;&gt; 之间进行转换
 * @since 2025/09/04 16:21:43
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(Map.class)
public class MapStringListLongTypeHandler extends BaseTypeHandler<Map<String, List<Long>>> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, List<Long>>> TYPE_REFERENCE = new TypeReference<>() {
    };

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map<String, List<Long>> parameter, JdbcType jdbcType) throws SQLException {
        try {
            String json = OBJECT_MAPPER.writeValueAsString(parameter);
            ps.setString(i, json);
        } catch (JsonProcessingException e) {
            throw new SQLException("Error converting Map<String, List<Long>> to JSON string", e);
        }
    }

    @Override
    public Map<String, List<Long>> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return parseJson(json);
    }

    @Override
    public Map<String, List<Long>> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return parseJson(json);
    }

    @Override
    public Map<String, List<Long>> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return parseJson(json);
    }

    private Map<String, List<Long>> parseJson(String json) throws SQLException {
        if (json == null || json.trim().isEmpty()) {
            return new HashMap<>();
        }

        try {
            return OBJECT_MAPPER.readValue(json, TYPE_REFERENCE);
        } catch (JsonProcessingException e) {
            throw new SQLException("Error parsing JSON string to Map<String, List<Long>>: " + json, e);
        }
    }

}
