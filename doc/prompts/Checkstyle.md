## 提示词：生成 Checkstyle 配置文件

你是 Java 代码规范专家，需要根据团队的编码风格规范生成 Checkstyle 规则文件。

### 风格规范摘要（来自 JAVA_STYLE.md）
- 缩进：4 个空格，禁止 Tab
- 最大行长度：150 字符
- 导入语句：禁止通配符导入 (import java.util.*)
- 命名约定：常量全大写，方法小驼峰，类名大驼峰
- 代码块：if/for/while 必须使用花括号，左花括号不换行，右花括号另起一行
- 异常处理：不允许空 catch 块，除非变量名为 expected 或 ignore
- 其他：移除未使用的导入，文件末尾留空行

### 输出要求
生成标准的 `checkstyle.xml` 文件，内容应包含以下模块：
- LineLength (150)
- AvoidStarImport
- ConstantName, MemberName, MethodName, TypeName
- NeedBraces
- LeftCurly, RightCurly (eol 风格)
- EmptyCatchBlock (排除变量名 expected|ignore)
- 并确保所有规则放在 TreeWalker 下。
请直接输出 XML 内容。