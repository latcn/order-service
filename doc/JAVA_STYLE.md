# JAVA_STYLE.md - 编码风格规范

## 命名
- 类名：大驼峰，名词，如 OrderApplicationService
- 方法名：小驼峰，动词开头，如 createOrder, findOrderById
- 持久层方法：findBy..., save, deleteBy... （严格遵循Spring Data JPA命名）
- DTO：后缀Request/Response，如 CreateOrderRequest
- 测试类：{ClassName}Test，测试方法：should_{预期行为}_when_{条件}

## 异常处理
- 业务异常统一抛出OrderException(OrderErrorCode errorCode)
- Controller切面捕获并转换为ApiResponse
- 禁止吞异常，必须记录日志后抛出或处理
- 日志：使用Lombok @Slf4j，关键业务点打印info，异常打印error并包含订单ID等上下文

## 注释
- 所有public方法必须有Javadoc，说明功能、参数、返回、可能的异常
- 复杂业务逻辑需添加行内注释

## 事务
- Application Service方法使用@Transactional(rollbackFor = Exception.class)
- 不允许在Controller或Domain Service中使用事务注解
- 只读操作加@Transactional(readOnly = true)

## 格式
- 缩进4空格，禁止Tab
- 行最大长度150字符
- 导入语句不要使用通配符