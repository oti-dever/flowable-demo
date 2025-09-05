package cn.cutepikachu.flowable.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @description
 * @since 2025/8/29 14:31:33
 */
@Data
public class ProcessDefinitionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识符
     */
    private String id;

    /**
     * 分类名称
     */
    private String category;

    /**
     * 显示名称
     */
    private String name;

    /**
     * 流程定义的唯一键名
     */
    private String key;

    /**
     * 流程描述
     */
    private String description;

    /**
     * 流程定义版本
     */
    private int version;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 部署ID
     */
    private String deploymentId;

    /**
     * 流程图资源名称
     */
    private String diagramResourceName;

    /**
     * 是否有启动表单
     */
    private boolean hasStartFormKey;

    /**
     * 是否有图形化表示
     */
    private boolean hasGraphicalNotation;

    /**
     * 是否暂停
     */
    private boolean suspended;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 派生来源
     */
    private String derivedFrom;

    /**
     * 根派生来源
     */
    private String derivedFromRoot;

    /**
     * 派生版本
     */
    private int derivedVersion;

    /**
     * 引擎版本
     */
    private String engineVersion;

}
