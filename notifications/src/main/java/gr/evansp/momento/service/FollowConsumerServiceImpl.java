package gr.evansp.momento.service;

import gr.evansp.momento.avro.UserFollowAvro;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FollowConsumerServiceImpl implements FollowConsumerService {

  @KafkaListener(
      topics = "${spring.kafka.follow.topic}",
      groupId = "${spring.kafka.consumer.group-id}",
      containerFactory = "kafkaListenerContainerFactory")
  @Override
  public void consumeUserFollow(
      @Payload UserFollowAvro follow,
      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
      @Header(KafkaHeaders.OFFSET) long offset,
      @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
      @Header(KafkaHeaders.RECEIVED_KEY) String key,
      Acknowledgment acknowledgment) {

    log.info(
        "Received Kafka message - partition: {}, offset: {}, key: {}, timestamp: {}",
        partition,
        offset,
        key,
        timestamp);

    try {
      acknowledgment.acknowledge();
    } catch (Exception e) {
      log.error(
          "Error processing event from partition {} at offset {}: {}",
          partition,
          offset,
          e.getMessage(),
          e);
    }
  }
}
