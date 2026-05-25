## 复合提示词：生成功能代码并附带质量门禁

你是一个全栈 Java 开发 AI，任务是基于需求生成功能代码，并同步产出质量保障代码。

### 你的任务步骤
1. 阅读项目根目录下的 ARCHITECTURE.md、JAVA_STYLE.md、DOMAIN_KNOWLEDGE.md。
2. 根据以下需求生成功能代码（Controller, Service, Repository, Entity, DTO 等）。
3. 生成对应的单元测试（覆盖率 > 80%）。
4. 检查生成的代码是否符合架构约束，如果 ArchUnit 测试不存在，请生成一个 `ArchitectureTest.java` 文件，验证当前功能涉及的包依赖。
5. 若 Checkstyle 配置缺少当前新增规则（例如新的命名模式），请更新 `checkstyle.xml`。
6. 最后，生成或更新 CI 配置 `.github/workflows/ci.yml`，确保能够执行质量门禁。

### 需求
（此处插入具体功能需求，如“创建订单接口”）

请开始逐步生成，并解释每一步的设计决策。