package gr.evansp.momento.service;

import gr.evansp.momento.avro.UserFollowAvro;
import gr.evansp.momento.model.UserFollow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class FollowProducerServiceImpl implements FollowProducerService {

    @Value("${spring.kafka.follow.topic}")
    String topic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public FollowProducerServiceImpl(KafkaTemplate<String, Object> template) {
        this.kafkaTemplate = template;
    }


    @Override
    public void sendMessage(UserFollow follow) {
        UserFollowAvro userFollow = UserFollowAvro.newBuilder()
                .setDate(follow.getCreatedAt().toLocalDate())
                .setFollowed(follow.getFollowed().getId().toString())
                .setFollows(follow.getFollows().getId().toString())
                .build();

        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(topic, follow.getId().toString(), userFollow);

        future.whenComplete((result, exception) -> {
            if (exception == null) {
                log.info("Sent Avro message=[{}] with offset=[{}]", userFollow.toString(), result.getRecordMetadata().offset());
            } else {
                log.error("Unable to send Avro message=[{}] due to : {}", userFollow.toString(), exception.getMessage());
            }
        });
    }
}
