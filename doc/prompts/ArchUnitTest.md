## 提示词：生成 ArchUnit 架构测试代码

你是 Java 后端测试专家，需要为 Spring Boot 项目编写 ArchUnit 架构测试，确保代码库严格遵守架构约定。

### 输入信息
- 项目包根路径：com.example.order
- 分层定义：
  - interfaces (..interfaces..)
  - application (..application..)
  - domain (..domain..)
  - infrastructure (..infrastructure..)
- 架构规则：
  1. 分层依赖：interfaces -> application -> domain <- infrastructure
  2. controller 不能直接访问 repository
  3. application 层的所有 public 方法必须有 @Transactional 注解
  4. domain 层不能依赖 Spring 或 JPA 注解

### 输出要求
请生成一个完整的 ArchUnit 测试类 `ArchitectureTest.java`，包含以下测试方法：
- `layer_dependencies_must_be_respected()`：使用 layeredArchitecture 验证分层依赖。
- `controllers_should_not_access_repositories()`：验证 controller 不访问 infrastructure 的 repository。
- `application_services_must_be_transactional()`：验证 application 包下所有 public 方法带有 @Transactional。
- `domain_should_not_depend_on_spring_or_jpa()`：验证 domain 包不依赖 org.springframework 和 jakarta.persistence。
- 代码中需包含必要的 import，并放在 `src/test/java/com/example/order/architecture/` 包下。
- 使用 JUnit 5 和 ArchUnit 库。