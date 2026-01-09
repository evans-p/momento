package gr.evansp.momento;

import com.redis.testcontainers.RedisContainer;
import gr.evansp.momento.repository.UserFollowRepository;
import gr.evansp.momento.repository.UserProfileRepository;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

/**
 * Abstract Class for Integration tests. Contains the initialization of
 * the required test containers.
 */
public abstract class AbstractIntegrationTest extends AbstractUnitTest {

  private static final Network network = Network.newNetwork();

  private static final PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:14")
          .withDatabaseName("user-management")
          .withUsername("postgres")
          .withPassword("postgres")
          .withExposedPorts(5432)
          .withInitScript("sql/ddl.sql")
          .withNetwork(network)
          .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 2));

  private static final KafkaContainer kafka =
      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"))
          .withKraft()
          .withNetwork(network)
          .withNetworkAliases("kafka");

  private static GenericContainer<?> schemaRegistry =
      new GenericContainer<>(DockerImageName.parse("confluentinc/cp-schema-registry:7.5.0"))
          .withNetwork(network)
          .withNetworkAliases("schema-registry")
          .dependsOn(kafka)
          .withExposedPorts(8081)
          .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry")
          .withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS", "kafka:9092")
          .withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:8081")
          .waitingFor(
              Wait.forHttp("/subjects")
                  .forStatusCode(200)
                  .withStartupTimeout(Duration.ofMinutes(2)));

  private static final RedisContainer redis =
      new RedisContainer(DockerImageName.parse("redis:7.4-alpine"))
          .withNetwork(network)
          .withNetworkAliases("redis")
          .withExposedPorts(6379);

  @Autowired UserProfileRepository userProfileRepository;

  @Autowired UserFollowRepository userFollowRepository;

  static {
    postgres.start();
    kafka.start();
    schemaRegistry.start();
    redis.start();
  }

  @DynamicPropertySource
  private static void registerPgProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);

    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);

    registry.add(
        "spring.kafka.producer.properties.schema.registry.url",
        () -> "http://" + schemaRegistry.getHost() + ":" + schemaRegistry.getMappedPort(8081));
    registry.add("spring.data.redis.host", redis::getHost);
    registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    registry.add("spring.cache.type", () -> "redis");
  }

  @BeforeEach
  @AfterEach
  void cleanup() {
    userFollowRepository.deleteAll();
    userProfileRepository.deleteAll();
  }
}
