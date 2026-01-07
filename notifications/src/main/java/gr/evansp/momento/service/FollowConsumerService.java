package gr.evansp.momento.service;

import gr.evansp.momento.avro.UserFollowAvro;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * Service interface for consuming user follow events from Kafka.
 * <p>
 * This service is responsible for processing {@link UserFollowAvro} messages consumed from
 * a Kafka topic. It handles the deserialization, validation, and processing of follow
 * relationship events, along with metadata about the message's origin and timing.
 * </p>
 * <p>
 * Implementations should handle message processing logic and manage manual acknowledgment
 * to ensure reliable message consumption and prevent message loss in case of processing failures.
 * </p>
 *
 * @author Momento Team
 * @version 1.0
 * @since 1.0
 * @see UserFollowAvro
 * @see org.springframework.kafka.annotation.KafkaListener
 */
public interface FollowConsumerService {

    /**
     * Consumes a user follow event from the Kafka topic with complete message metadata.
     * <p>
     * This method processes incoming {@link UserFollowAvro} messages along with their
     * associated Kafka metadata including partition, offset, timestamp, and message key.
     * The method uses manual acknowledgment to ensure messages are only committed after
     * successful processing, providing at-least-once delivery semantics.
     * </p>
     * <p>
     * Implementations should:
     * <ul>
     *   <li>Validate the incoming {@code follow} event</li>
     *   <li>Execute business logic for processing the follow relationship</li>
     *   <li>Call {@link Acknowledgment#acknowledge()} only after successful processing</li>
     *   <li>Handle exceptions appropriately without acknowledging failed messages</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Note:</strong> If processing fails and the message is not acknowledged,
     * Kafka will redeliver the message according to the consumer's retry configuration.
     * </p>
     *
     * @param follow the {@link UserFollowAvro} message payload containing the follow
     *               relationship data. Must not be {@code null}.
     * @param partition the Kafka partition number from which the message was consumed.
     *                  Useful for tracking message distribution and debugging.
     * @param offset the message offset within the partition. This represents the
     *               position of the message in the Kafka log and can be used for
     *               tracking processing progress or implementing idempotency.
     * @param timestamp the timestamp when the message was produced to Kafka, in
     *                  milliseconds since epoch. This is the broker timestamp, not
     *                  the event creation time.
     * @param key the message key used for partitioning. Typically contains a user ID
     *            or identifier to ensure related messages are consumed in order.
     *            May be {@code null} if the message was produced without a key.
     * @param acknowledgment the {@link Acknowledgment} object used to manually commit
     *                       the message offset after successful processing. Must be
     *                       called to prevent message reprocessing.
     * @throws IllegalArgumentException if {@code follow} is {@code null} or invalid
     * @throws org.springframework.kafka.KafkaException if there is an error during message consumption
     * @see UserFollowAvro
     * @see Acknowledgment#acknowledge()
     * @see KafkaHeaders
     */
    void consumeUserFollow(@Payload UserFollowAvro follow,
                           @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                           @Header(KafkaHeaders.OFFSET) long offset,
                           @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                           @Header(KafkaHeaders.RECEIVED_KEY) String key,
                           Acknowledgment acknowledgment);
}
