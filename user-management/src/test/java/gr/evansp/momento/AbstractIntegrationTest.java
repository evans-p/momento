package gr.evansp.momento;

import gr.evansp.momento.repository.UserFollowRepository;
import gr.evansp.momento.repository.UserProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * Abstract Class for Integration tests. Contains the initialization of
 * the required test containers.
 */
public abstract class AbstractIntegrationTest extends AbstractUnitTest {

  private static final PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:14")
          .withDatabaseName("user-management")
          .withUsername("postgres")
          .withPassword("postgres")
          .withExposedPorts(5432)
          .withInitScript("sql/ddl.sql")
          .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 2));

  static {
    postgres.start();
  }

  @Autowired UserProfileRepository userProfileRepository;

  @Autowired UserFollowRepository userFollowRepository;

  @DynamicPropertySource
  private static void registerPgProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add(
        "spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
  }

  @BeforeEach
  @AfterEach
  void cleanup() {
    userFollowRepository.deleteAll();
    userProfileRepository.deleteAll();
  }
}
