# What is this?
This is a POC (Proof of Concept) project that demonstrates the usage of Redis for implementing a distributed lock in an e-commerce system. 
The project showcases how the distributed lock can be used to prevent overselling scenarios.

# Getting Started
## Prerequisites
To run this project, you need to have the following prerequisites installed:

- Docker: Used for building the Docker image.
- Kubernetes: Used for deploying the Redis and order service components.

## Run
1. Build docker image
```
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=order-service:0.0.1
```

2. Deploy to kubernetes
```
kubectl create namespace order
kubectl apply -f k8s/redis-deployment.yaml
kubectl apply -f k8s/order-service-deployment.yaml
```

3. Set stock value in Redis, for example: 5, that means there are 5 items in stock.


## Findings
Once the project is deployed and running, you can test the functionality of the distributed lock by making orders through the order service. The result will depend on the current stock availability.

### Test run
#### Asspumptions
- Stock value in Redis is 10 - that means 10 items are in stock.
- Order service is deployed with 3 replicas.
- 40 orders are made concurrently using jmeter.

#### Result
- 10 orders are successful.
- 30 orders are failed due to stock not available.

| Container | Order success | Order failed |
|-----------|---------------|--------------|
| Replica 1 | 3             | 12           |
| Replica 2 | 2             | 10           |
| Replica 3 | 5             | 8            |
| Total     | 10            | 30           |


Replica 1: 3 orders are successful, 12 orders are failed.
```
2023-05-13T16:00:43.244Z  INFO 1 --- [io-8080-exec-19] c.e.d.order.OrderService                 : Order created successfully, Stock left 9
2023-05-13T16:00:43.828Z  INFO 1 --- [io-8080-exec-18] c.e.d.order.OrderService                 : Order created successfully, Stock left 8
2023-05-13T16:00:45.676Z  INFO 1 --- [nio-8080-exec-1] c.e.d.order.OrderService                 : Order created successfully, Stock left 6
2023-05-13T16:00:46.089Z  INFO 1 --- [io-8080-exec-20] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.104Z  INFO 1 --- [nio-8080-exec-3] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.156Z  INFO 1 --- [nio-8080-exec-6] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.189Z  INFO 1 --- [io-8080-exec-17] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.302Z  INFO 1 --- [io-8080-exec-18] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.369Z  INFO 1 --- [io-8080-exec-19] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.393Z  INFO 1 --- [nio-8080-exec-9] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.425Z  INFO 1 --- [io-8080-exec-15] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.548Z  INFO 1 --- [io-8080-exec-11] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.646Z  INFO 1 --- [nio-8080-exec-3] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.664Z  INFO 1 --- [io-8080-exec-20] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.686Z  INFO 1 --- [nio-8080-exec-6] c.e.d.order.OrderService                 : Out of stock, Stock left 0
```

Replica 2: 2 orders are successful, 10 orders are failed.
```
2023-05-13T16:00:45.586Z  INFO 1 --- [io-8080-exec-16] c.e.d.order.OrderService                 : Order created successfully, Stock left 7
2023-05-13T16:00:45.821Z  INFO 1 --- [io-8080-exec-11] c.e.d.order.OrderService                 : Order created successfully, Stock left 4
2023-05-13T16:00:46.123Z  INFO 1 --- [io-8080-exec-25] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.227Z  INFO 1 --- [nio-8080-exec-1] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.248Z  INFO 1 --- [nio-8080-exec-3] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.323Z  INFO 1 --- [io-8080-exec-21] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.493Z  INFO 1 --- [io-8080-exec-22] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.608Z  INFO 1 --- [nio-8080-exec-2] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.723Z  INFO 1 --- [nio-8080-exec-8] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.799Z  INFO 1 --- [io-8080-exec-18] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.809Z  INFO 1 --- [io-8080-exec-24] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.828Z  INFO 1 --- [nio-8080-exec-5] c.e.d.order.OrderService                 : Out of stock, Stock left 0
```

Replica 3: 5 orders are successful, 8 orders are failed.
```
2023-05-13T16:00:45.753Z  INFO 1 --- [io-8080-exec-10] c.e.d.order.OrderService                 : Order created successfully, Stock left 5
2023-05-13T16:00:45.890Z  INFO 1 --- [nio-8080-exec-6] c.e.d.order.OrderService                 : Order created successfully, Stock left 3
2023-05-13T16:00:45.927Z  INFO 1 --- [nio-8080-exec-4] c.e.d.order.OrderService                 : Order created successfully, Stock left 2
2023-05-13T16:00:46.013Z  INFO 1 --- [io-8080-exec-14] c.e.d.order.OrderService                 : Order created successfully, Stock left 1
2023-05-13T16:00:46.044Z  INFO 1 --- [io-8080-exec-12] c.e.d.order.OrderService                 : Order created successfully, Stock left 0
2023-05-13T16:00:46.284Z  INFO 1 --- [io-8080-exec-15] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.347Z  INFO 1 --- [nio-8080-exec-5] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.449Z  INFO 1 --- [nio-8080-exec-9] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.570Z  INFO 1 --- [nio-8080-exec-6] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.623Z  INFO 1 --- [nio-8080-exec-4] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.738Z  INFO 1 --- [nio-8080-exec-2] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.757Z  INFO 1 --- [io-8080-exec-12] c.e.d.order.OrderService                 : Out of stock, Stock left 0
2023-05-13T16:00:46.783Z  INFO 1 --- [io-8080-exec-14] c.e.d.order.OrderService                 : Out of stock, Stock left 0
```


If the stock is available, an order will be created successfully, and the stock count will be decremented.
If the stock is depleted, the order creation will fail, indicating an out-of-stock status.
You can check the logs or monitoring system to observe the order creation process and the stock count changes.

Feel free to explore and modify the project code to further understand the implementation details of the distributed lock using Redis.