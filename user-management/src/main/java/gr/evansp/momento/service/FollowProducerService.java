package gr.evansp.momento.service;


import gr.evansp.momento.model.UserFollow;

/**
 * Service interface for producing user follow events to Kafka.
 * <p>
 * This service is responsible for publishing {@link UserFollow} events to a Kafka topic,
 * enabling asynchronous processing and decoupling of follow operations from downstream
 * consumers. The events can be consumed by other services for analytics, notifications,
 * or maintaining follower relationships.
 * </p>
 *
 * @author Momento Team
 * @version 1.0
 * @since 1.0
 */
public interface FollowProducerService {

     /**
      * Sends a user follow event to the Kafka topic.
      * <p>
      * This method publishes a {@link UserFollow} message to the configured Kafka topic
      * asynchronously. The message contains information about a follow relationship between
      * two users. The actual delivery and acknowledgment are handled by the underlying
      * Kafka producer implementation.
      * </p>
      * <p>
      * The method returns immediately and does not block waiting for the message to be sent.
      * Implementations should handle serialization, partitioning, and error logging internally.
      * </p>
      *
      * @param follow the {@link UserFollow} object containing the follow relationship data
      *               to be published to Kafka. Must not be {@code null}.
      * @see UserFollow
      */
     void sendMessage(UserFollow follow);
}
