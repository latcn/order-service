# DOMAIN_KNOWLEDGE.md - 订单领域知识

## 核心实体
- Order：订单，包含订单号(orderNo)、总金额(totalAmount)、状态(status)、创建时间、订单项列表。
- OrderItem：订单项，关联商品ID(productId)、商品名(productName)、单价(unitPrice)、数量(quantity)。
- OrderStatus：枚举，CREATED, PAID, CANCELLED。

## 业务规则
1. 创建订单时，总金额 = sum(每个订单项的 unitPrice * quantity)。不依赖前端传入的总金额，后端必须重新计算。
2. 订单初始状态为 CREATED。
3. 订单号生成规则：前缀"ORD"+年月日+6位随机数（使用OrderNumberGenerator领域服务）。
4. 库存扣减为外部服务调用，本文示例暂不实现（使用占位符接口）。
5. 同一商品在一个订单中不可重复出现，以productId区分。
6. 订单项数量必须大于0。
7. 创建订单操作需记录日志（订单创建成功/失败）。