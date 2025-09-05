# Flowable Demo

1. `IFlowableService` 封装各类 Flowable 服务方法
2. `/service`
   - `IProcessService` 通用流程服务接口
   - `AbstractProcessService` 通用流程服务抽象类实现，所有流程业务 Service 均需要继承
3. `/strategy` 审批人选择策略
   - `IApproverSelectStrategy` 审批人选择策略接口
   - `ApproverSelectStrategyFactory` 审批人选择策略工厂
   - `/impl` 审批人选择策略具体实现
4. `/listener` 监听器
   - `ITaskListener` 用户任务（审批）
     - `AbstractTaskListener` 任务监听器抽象基类
   - `/task` 任务监听器实现
   - `IProcessEventListener` 流程事件监听器接口，用于监听整个流程的结束和取消事件
     - `AbstractProcessEventListener` 流程事件监听器抽象基类
   - `/process` 流程事件监听器实现
   - `/manager` 流程事件监听器管理器
   - `AbstractExecutionListener` 执行事件监听器抽基类（监听包所有自组建的 开始/结束/流转 事件）
5. `/delegate`
   - `IDelegate` 委托接口，处理组建委托执行操作
   - `AbstractProcessExpireDelegate` 流程超时处理委托抽象类
   - `/impl` 委托实现类
6. `/event` 事件对象
   - `ProcessEvent` 事件对象接口，用于 `IProcessService` 实现类中业务处理方法的返回值
   - `/impl`
     - `ProcessStartedEvent` 流程发起事件对象
     - `TaskApprovedEvent` 任务审批事件对象