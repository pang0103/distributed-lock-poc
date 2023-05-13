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
kubectl apply -f k8s/redis-deployment.yaml
kubectl apply -f k8s/order-service-deployment.yaml
```

3. Set stock value in Redis, for example: 5, that means there are 5 items in stock.


## Result
Once the project is deployed and running, you can test the functionality of the distributed lock by making orders through the order service. The result will depend on the current stock availability.

If the stock is available, an order will be created successfully, and the stock count will be decremented.
If the stock is depleted, the order creation will fail, indicating an out-of-stock status.
You can check the logs or monitoring system to observe the order creation process and the stock count changes.

Feel free to explore and modify the project code to further understand the implementation details of the distributed lock using Redis.