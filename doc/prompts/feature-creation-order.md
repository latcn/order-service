## 提示词：实现订单创建功能

你是一个资深Java后端开发，需要在一个Spring Boot项目中实现创建订单功能。请严格按照项目架构和编码规范生成代码。

### 背景
项目采用DDD四层架构，已有架构文档ARCHITECTURE.md，编码规范JAVA_STYLE.md，领域知识DOMAIN_KNOWLEDGE.md。请先阅读并理解这三个文档。

### 需求（精确需求）
实现POST /api/orders接口，接收CreateOrderRequest JSON：
- 包含订单项列表，每一项有productId, productName, unitPrice (BigDecimal), quantity (int)
- 返回统一响应体ApiResponse<OrderResponse>，包含订单号、总金额、状态、创建时间、订单项

验收标准：
- 总金额由后端计算，不使用请求中的值
- 订单状态初始为CREATED
- 订单号生成规则：ORD+当天日期yyyyMMdd+6位随机数
- 重复productId校验，抛出订单项重复异常（OrderErrorCode.DUPLICATE_ORDER_ITEM）
- 数量必须>0，否则抛出OrderErrorCode.INVALID_QUANTITY
- 需使用@Valid校验请求参数非空

### 架构约束
- Controller只做参数校验和调用ApplicationService，不得包含业务逻辑
- ApplicationService负责@Transactional，调用DomainService计算金额，调用Repository保存
- Domain层：Order实体拥有addItem(OrderItem item)方法，内部校验重复和数量；OrderDomainService提供calculateTotal方法
- Infrastructure层：OrderRepository继承JpaRepository

### 风格要求
- 严格遵循JAVA_STYLE.md，包括命名、异常处理（使用OrderErrorCode枚举）、日志、事务
- 使用Lombok简化代码（@Data, @Builder, @Slf4j等）
- 所有public方法写Javadoc

### 输出要求
请按以下顺序生成代码文件，并说明关键设计点：
1. CreateOrderRequest.java
2. OrderResponse.java
3. OrderErrorCode.java（如果不存在则新增）
4. Order.java（实体更新）
5. OrderItem.java
6. OrderStatus.java（如果不存在）
7. OrderDomainService.java
8. OrderApplicationService.java
9. OrderController.java
10. OrderRepository.java
同时提供对应的单元测试，覆盖率80%以上。