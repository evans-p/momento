package gr.evansp.momento;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import gr.evansp.momento.repository.AssetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * Abstract Class for Integration tests. Contains the initialization of
 * the required test containers.
 */
public abstract class AbstractIntegrationTest extends AbstractUnitTest {

	private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
			                                                       .withDatabaseName("cdn")
			                                                       .withUsername("postgres")
			                                                       .withPassword("postgres")
			                                                       .withExposedPorts(5432)
			                                                       .withInitScript("sql/ddl.sql")
			                                                       .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 2));

	static {
		postgres.start();
	}

	@Autowired
	protected AssetRepository repository;

	@Value("${cdn.storage.location}")
	protected String storageLocation;

	@DynamicPropertySource
	private static void registerPgProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
		registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
	}

	@BeforeEach
	@AfterEach
	public void cleanup() throws IOException {
		repository.deleteAll();

		Files.walk(Paths.get(storageLocation))
				.filter(Files::isRegularFile)
				.forEach(path -> {
					try {
						Files.deleteIfExists(path);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
	}
}
