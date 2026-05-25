## 提示词：生成 GitHub Actions CI 流水线

你需要为 Maven 多模块项目创建 CI 工作流，确保每次推送和 PR 都执行完整质量门禁。

### 项目环境
- Java 17
- Maven 3.9
- 包含 Checkstyle 插件、ArchUnit 测试、JaCoCo 覆盖率、SonarQube 扫描

### 流水线要求
1. 触发条件：push 到 main 和 PR 到 main
2. Job 步骤：
   - 检出代码
   - 设置 JDK 17
   - 缓存 Maven 依赖
   - 执行 mvn verify (包含测试和 Checkstyle)
   - 单独运行 ArchUnit 测试组（-Dtest=ArchitectureTest）
   - 生成 JaCoCo 报告
   - 执行 SonarQube 分析（使用 secrets.SONAR_TOKEN）
3. 若任何步骤失败，阻止合并。
4. 工作流文件命名为 ci.yml，放在 .github/workflows/ 目录。

### 输出要求
输出完整的 YAML 内容，包含合理的名称和注释。