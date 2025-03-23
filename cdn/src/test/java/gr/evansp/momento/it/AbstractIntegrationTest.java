package gr.evansp.momento.it;


import org.junit.ClassRule;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * Abstract Class for Integration tests. Contains the initialization of
 * the required test containers.
 */
public abstract class AbstractIntegrationTest {

	@ClassRule
	public static PostgreSQLContainer<?> postgres =new PostgreSQLContainer<>("postgres:14")
				.withDatabaseName("cdn")
				.withUsername("postgres")
				.withPassword("postgres")
				.withExposedPorts(5432)
                .withInitScript("ddl.sql")
				.waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 2));


	@DynamicPropertySource
	static void registerPgProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
		registry.add("spring.jpa.properties.hibernate.dialect", ()-> "org.hibernate.dialect.PostgreSQLDialect");
	}
}
