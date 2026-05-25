## 提示词：使用 TDD 实现“创建订单”功能

你是项目组的 Java TDD 实践者，请基于以下信息开发订单创建功能。

### 预置上下文（AI 应已读取）
- `ARCHITECTURE.md`：四层架构，事务边界，分层约束。
- `JAVA_STYLE.md`：命名、异常、日志、格式规范。
- `DOMAIN_KNOWLEDGE.md`：订单实体、业务规则（总金额计算、订单号生成、重复项校验、数量校验等）。

### 采用 TDD，请执行以下步骤：

#### 第1轮：测试正常创建订单
1. **测试用例**：提供合法的 `CreateOrderRequest`（包含两个不同 productId 的订单项，数量均>0），期望返回 200 及 `OrderResponse`，其中总金额为 unitPrice*quantity 之和，状态为 CREATED。
2. **红灯验证**：此时没有 Controller 或 Service 实现，测试会编译错误，请说明如何处理（建议先生成接口骨架）。
3. **绿灯实现**：创建必需的类骨架（Controller, ApplicationService, DomainService, Repository 接口），使测试能通过，但实体内部逻辑暂简单实现。
4. **重构**：检查命名、包结构是否符合架构，进行必要调整。

#### 第2轮：测试重复产品ID校验
1. **测试用例**：请求包含两个 productId 相同的订单项，期望抛出 `OrderException(DUPLICATE_ORDER_ITEM)`。
2. **红灯**：因尚未实现校验，测试会失败。
3. **绿灯**：在 `Order.addItem()` 方法中添加重复性检查。
4. **重构**：确认异常抛出符合统一错误码规范。

#### 第3轮：测试数量<=0校验
1. **测试用例**：订单项数量为0，期望抛出 `OrderException(INVALID_QUANTITY)`。
2. **红灯/绿灯/重构**同上。

#### 第4轮：测试总金额计算（隔离 Domain 逻辑）
1. **测试用例**：直接测试 `OrderDomainService.calculateTotal(List<OrderItem>)`，给定3个订单项，验证总金额计算结果。
2. **绿灯**：在 DomainService 中实现累加逻辑。
3. **重构**：评估是否需要用 Money 值对象封装 BigDecimal。

### 输出要求
- 每轮对话请给出测试类全代码、生产代码全代码，并注明红色/绿色状态。
- 所有测试类放在 `src/test/java/com/example/order/unit/` 下。
- 所有生产代码需带有完整的 Javadoc 和日志。