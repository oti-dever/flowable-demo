# Flowable Demo

1. `/service`
    - `IFlowableService` 封装各类 Flowable 服务方法
    - `IProcessService` 流程服务通用接口
    - `IProcessBusinessService` 流程业务服务接口
    - `ProcessBusinessServiceManager` 流程业务服务管理器
2. `/strategy` 审批人选择策略
    - `IApproverSelectStrategy` 审批人选择策略接口
    - `ApproverSelectStrategyFactory` 审批人选择策略工厂
    - `/impl` 审批人选择策略具体实现
3. `/listener` 监听器
    - `ITaskListener` 用户任务（审批任务）监听器接口
        - `AbstractTaskListener` 任务监听器抽象基类
    - `IProcessEventListener` 流程事件监听器接口，用于监听整个流程的完成（通过）和删除（不通过、撤销、废弃）事件
        - `AbstractProcessEventListener` 流程事件监听器抽象基类
    - `AbstractExecutionListener` 执行事件监听器抽基类（监听包所有自组建的 开始/结束/流转 事件）
    - `/task` 任务监听器实现
    - `/process` 流程事件监听器实现
    - `/manager`
        - `ProcessEventListenerManager` 流程事件监听器管理器
4. `/delegate`
    - `IDelegate` 委托接口，处理组建委托执行操作
    - `AbstractProcessExpireDelegate` 流程超时处理委托抽象类
    - `/impl` 委托实现类
5. `/event` 事件对象
    - `ProcessEvent` 事件对象接口，用于 `IProcessBusinessService` 流程业务服务实现类中业务处理方法的返回值
    - `/impl`
        - `ProcessStartedEvent` 流程发起事件对象
        - `TaskApprovedEvent` 任务审批事件对象