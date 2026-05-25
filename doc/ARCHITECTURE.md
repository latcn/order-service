# ARCHITECTURE.md - 订单服务架构决策

## 分层架构（DDD轻量级）
本项目采用四层架构，严格遵循依赖方向：interfaces -> application -> domain <- infrastructure

### 各层职责
1. **interfaces**：处理HTTP请求，参数校验，调用application service，返回响应。  
   - 不允许包含业务逻辑，不得直接访问Repository。
   - Controller接收application层的DTO，直接返回给前端。
2. **application**：流程编排，事务边界控制（@Transactional），处理应用异常，DTO定义与转换。  
   - 可以调用domain services和infrastructure interfaces。
   - DTO（Request/Response）定义在此层，负责领域对象与DTO的转换。
3. **domain**：核心业务逻辑，实体、值对象、领域服务。  
   - 不依赖任何外部框架，纯Java对象。领域服务通过接口依赖基础设施（依赖倒置）。
4. **infrastructure**：实现domain层定义的仓储接口，数据库操作，外部API调用。  
   - 可以依赖Spring Data JPA等框架。

### 关键约束
- Controller方法只做：接收请求 -> 校验 -> 调用应用服务 -> 返回application层提供的DTO。
- Application Service方法添加@Transactional，负责领域对象与DTO的转换，并在方法内完成所有数据库写操作。
- 实体使用JPA注解，但业务关键方法不要依赖JPA回调，应显式调用。
- 金额字段统一使用BigDecimal，不可使用float/double。
- 所有API返回统一响应体格式（见ApiResponse类）。
- 异常统一使用OrderErrorCode枚举，实现ErrorCode接口，全局异常处理器解析。
- DTO（CreateOrderRequest、OrderResponse等）定义在application层，interfaces层仅负责HTTP交互。