package cn.cutepikachu.flowable.util;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/9/2 17:46:43
 */
public class TaskAssigneeMapUtil {

    public static Map<String, List<Long>> createTaskAssigneeMap() {
        return new HashMap<>();
    }

    public static Map<String, List<Long>> parseTaskAssigneeMap(String taskAssigneeMapStr) {
        return JSONUtil.toBean(taskAssigneeMapStr, new TypeReference<Map<String, List<Long>>>() {
        }.getType(), true);
    }

    public static List<Long> getTaskAssignee(Map<String, List<Long>> map, String taskKey) {
        return map.getOrDefault(taskKey, Collections.emptyList());
    }

    public static void setTaskAssignee(Map<String, List<Long>> map, String taskKey, List<Long> assigneeList) {
        map.put(taskKey, assigneeList);
    }

    public static void addTaskAssignee(Map<String, List<Long>> map, String taskKey, List<Long> assigneeList) {
        List<Long> assignee = map.getOrDefault(taskKey, Collections.emptyList());
        assignee.addAll(assigneeList);
        map.put(taskKey, assignee);
    }

    public static void removeTask(Map<String, List<Long>> map, String taskKey) {
        map.remove(taskKey);
    }

    public static void removeAssignee(Map<String, List<Long>> map, String taskKey, Long assignee) {
        List<Long> assigneeList = map.get(taskKey);
        if (assigneeList == null) {
            return;
        }
        assigneeList.remove(assignee);
    }

}
