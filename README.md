<p align="center">
  <img alt="logo" src="assets/logo.png" width="150" style="margin-top:100px"/>
</p>

# Momento


## Description
Momento is a social media like app, built in java.

## Initial Idea
- Microservices:
    - User Service: Manages user profiles and authentication.
    - Post Service: Handles post creation, retrieval, and updates.
    - Feed Service: Aggregates posts from followed users.
    - Notification Service: Sends notifications for new posts, likes, and comments.

- Apache Kafka
    - Post creation events are published to a Kafka topic.
    - Feed service subscribes to this topic to update user feeds.
    - Like and comment events are also published to kafka.
    - Notification service subscribes to like, comment and post topics.

- Nginx
    - Routes requests to the appropriate microservices.
    - Handles static content.

- Rate Limiting
    - Protects API endpoints from excessive requests.

- Hibernate
    - Stores user profiles and post data.

- Redis
    - Caches user feeds for fast retrieval.
    - Stores user session data.
    - Stores active user information.

- Protocol Buffers
    - Used for communication between microservices and for Kafka message serialization.