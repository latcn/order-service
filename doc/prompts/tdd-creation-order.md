# TDD 提示词：创建订单功能 (tdd-creation-order.md)

## 文档目的

本提示词用于在订单服务项目中采用**测试驱动开发（TDD）**实现“创建订单”功能。
它严格遵循项目质量公式：

**高质量代码 ≈ (架构 + 风格 + 约束) × (精确需求 + 领域知识 + 交互式迭代) × 专家判断力**

所有 TDD 步骤都将基于项目已有的 `ARCHITECTURE.md`、`JAVA_STYLE.md` 和 `DOMAIN_KNOWLEDGE.md`，并在迭代过程中嵌入专家审查点。

---

## 使用说明

1. **前置条件**：确保 AI 编程助手已读取以下文件：
   - `ARCHITECTURE.md`（分层架构与依赖规则）
   - `JAVA_STYLE.md`（命名、异常、日志、事务风格）
   - `DOMAIN_KNOWLEDGE.md`（订单领域实体与业务规则）
2. **交互方式**：建议分多轮对话，严格按照本提示词的步骤执行，每轮只处理一个测试场景。
3. **专家职责**：在标注 **【专家判断点】** 的环节，由资深研发人员介入审核，确认方向后再继续。

---

## TDD 开发提示词正文

### 角色与约束

你是一名严格践行 TDD 的 Java 高级工程师。项目采用 DDD 四层架构，你必须保证每一次提交的代码都通过所有测试与架构约束。

### 核心流程（必须逐步骤输出）

你将按照以下循环开发“创建订单”功能，每次循环对应一个测试场景：

1. **红灯（Red）**：编写一个失败的测试用例，并说明预期失败原因。
2. **绿灯（Green）**：编写刚好让测试通过的最小实现代码，不允许提前设计未测试的功能。
3. **重构（Refactor）**：审查当前实现，消除代码异味，优化结构，确保仍然通过测试。
4. **架构自检**：检查当前代码是否违反 `ARCHITECTURE.md` 或 `JAVA_STYLE.md`，若有违规立即修复。
5. **专家审查**：在关键节点暂停，等待专家确认。

---

### 开发需求（精确需求）

实现 **POST /api/orders** 接口，具体规则来自 `DOMAIN_KNOWLEDGE.md`：
- 接收 `CreateOrderRequest`，包含订单项列表（productId, productName, unitPrice, quantity）。
- 后端必须重新计算总金额 = sum(unitPrice * quantity)。
- 订单初始状态为 `CREATED`。
- 订单号由 `OrderNumberGenerator` 领域服务生成，格式“ORD + yyyyMMdd + 6 位随机数”。
- 同一订单内不可出现相同 productId，否则抛出 `OrderErrorCode.DUPLICATE_ORDER_ITEM`。
- 订单项数量必须 > 0，否则抛出 `OrderErrorCode.INVALID_QUANTITY`。
- 返回统一响应体 `ApiResponse<OrderResponse>`。

---

### 第 1 轮：正常创建订单

#### 步骤 1：编写测试用例（红灯）

- 测试场景：提供两个不同 productId、数量 >0 的合法订单项，验证返回 200，响应体包含正确总金额、状态 CREATED、订单号非空。
- 测试类型：`@WebMvcTest(OrderController.class)` 集成测试，mock `OrderApplicationService`，验证 HTTP 交互正确性。
- 红灯说明：此时 `OrderController`、`CreateOrderRequest`、`OrderResponse` 均不存在，测试无法编译，满足红色状态。

#### 步骤 2：最小实现（绿灯）

创建以下骨架文件（不包含具体业务逻辑，只保证测试编译和通过）：
- `CreateOrderRequest`（含 Lombok @Data, @Valid 注解）
- `OrderResponse`（含订单号、总金额、状态、订单项列表）
- `OrderController`（注入 `OrderApplicationService`，方法体简单委托）
- `OrderApplicationService`（接口 + 空实现返回 new OrderResponse()）

#### 步骤 3：重构

- 检查包路径是否符合 `interfaces` / `application` 分层。
- 确认 `ApiResponse` 统一封装是否已存在，若不存在，补全并调整测试断言。

#### 【专家判断点 1】
- 审查分层结构：Controller 是否只做了调用？是否需要在此轮引入异常处理器？
- 确认 `ApiResponse` 格式与团队其他服务一致。

---

### 第 2 轮：重复产品 ID 校验

#### 步骤 1：编写测试用例（红灯）

- 测试场景：请求包含两个 productId 相同的订单项，期望抛出 `OrderException(OrderErrorCode.DUPLICATE_ORDER_ITEM)`，返回对应错误码。
- 测试类型：`OrderDomainService` 或 `Order` 实体单元测试（隔离 Spring）。
- 红灯说明：当前未实现校验，测试将失败。

#### 步骤 2：最小实现（绿灯）

- 在 `Order` 实体中添加 `addItem(OrderItem item)` 方法，用 Set 或 Map 检查 productId 重复性，违反则 throw `new OrderException(OrderErrorCode.DUPLICATE_ORDER_ITEM)`。
- 调整创建流程，使订单项通过 `addItem` 加入。

#### 步骤 3：重构

- 将错误信息国际化（若项目要求），或至少在异常中携带 productId 日志。
- 确保 `OrderException` 继承 `RuntimeException` 并持有 `OrderErrorCode`，符合 `JAVA_STYLE.md`。

#### 【专家判断点 2】
- 审查异常是否被全局处理器正确捕获并转换为统一响应。
- 考虑未来可能需要并发校验，但当前不提前设计。

---

### 第 3 轮：数量非法校验

#### 步骤 1：编写测试用例（红灯）

- 测试场景：订单项 quantity = 0（或负数），期望抛出 `OrderException(OrderErrorCode.INVALID_QUANTITY)`。

#### 步骤 2：最小实现（绿灯）

- 在 `Order.addItem()` 或 `OrderItem` 构造中加入 `if (quantity <= 0) throw ...`。

#### 步骤 3：重构

- 如校验逻辑分散，可提取 `OrderItem.validate()` 方法。

#### 【专家判断点 3】
- 检查 `OrderItem` 是否仍然是贫血模型，如果是，考虑封装构造工厂或静态创建方法，但不过度设计。

---

### 第 4 轮：总金额计算（领域逻辑）

#### 步骤 1：编写测试用例（红灯）

- 测试场景：给定 3 个订单项，直接调用 `OrderDomainService.calculateTotal(...)`，验证返回的 `BigDecimal` 等于手动计算结果。
- 红灯说明：该方法尚未实现。

#### 步骤 2：最小实现（绿灯）

- 实现 `OrderDomainService.calculateTotal(List<OrderItem> items)`，使用 Stream 对 `unitPrice * quantity` 求和，注意使用 `BigDecimal` 的精确运算。

#### 步骤 3：重构

- 考虑是否需要 `Money` 值对象封装金额（根据团队规范决定）。若不引入，至少提取 `multiply` 方法避免重复。

#### 【专家判断点 4】
- 确认金额计算未使用 `double`。
- 决定是否引入 `Money` 类型，避免 `BigDecimal` 裸用。

---

### 第 5 轮：应用服务集成与事务

#### 步骤 1：编写测试用例（红灯）

- 测试场景：使用 `MockMvc` 调用完整流程，验证应用服务 `createOrder` 方法被调用一次，并模拟 repository 返回保存的订单。
- 红灯说明：应用服务尚未集成 domain 服务和 repository。

#### 步骤 2：最小实现（绿灯）

- 完善 `OrderApplicationService.createOrder(CreateOrderRequest req)`：
  - 调用 `OrderNumberGenerator.generate()` 获取订单号。
  - 构建 `Order` 实体，调用 `addItem` 逐项添加。
  - 调用 `OrderDomainService.calculateTotal()` 计算总金额。
  - 调用 `OrderRepository.save(order)` 持久化。
  - 返回 `OrderResponse`。
- 方法必须标注 `@Transactional(rollbackFor = Exception.class)`。

#### 步骤 3：重构

- 确保日志打印了关键业务节点（`@Slf4j` + log.info）。
- 检查是否满足“写操作必须发生在事务内”的约束。

#### 【专家判断点 5】
- 验证事务边界：repository 是否正确注入？Spring 代理是否生效（避免内部调用失效）？
- 评估是否需要对 `OrderNumberGenerator` 进行幂等处理（如失败重试造成号段浪费，现阶段先忽略）。

---

## 专家判断力总览

在以上所有轮次完成后，资深研发人员必须执行最终审查：

1. **架构一致性**：运行 `mvn test -Dtest=ArchitectureTest`，确保分层依赖未被破坏。
2. **风格合规**：运行 `mvn checkstyle:check`，确保无严重违规。
3. **测试有效性**：人工检查测试用例是否真正验证了业务规则，杜绝“伪绿”。
4. **性能预留**：评估订单项数量上限，若可能极大，需考虑批量 `saveAll`。
5. **安全与异常**：检查是否泄漏了敏感字段（如内部异常栈），错误码体系是否完整。
6. **文档产出**：确保 API 文档与代码同步（可后续补充）。

专家有权在任何阶段要求 AI 返工、修改测试结构或调整实现策略，直到满足质量基线。

---

## 输出格式要求

AI 在每轮对话中应按以下格式输出：
