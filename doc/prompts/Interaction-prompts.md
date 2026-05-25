
1. 基于prompts/feature-creation-order.md生成代码骨架，只包含类定义、方法签名和Javadoc，不实现具体逻辑。确保包结构和依赖正确。

2. 现在实现Order实体中的addItem方法，要求校验重复productId和数量>0，否则抛出对应OrderErrorCode异常。日志记录添加操作。

3. 实现OrderApplicationService.createOrder方法，根据之前的骨架完成事务协调逻辑：调用领域服务计算金额、生成订单号、保存。注意使用OrderNumberGenerator（注入的领域服务接口）。

4. 为OrderApplicationService.createOrder编写单元测试，覆盖正常流程、金额计算、重复项异常、数量非法异常、仓储交互验证。使用Mockito。

