package gr.evansp.momento.repository;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import gr.evansp.momento.AbstractIntegrationTest;
import gr.evansp.momento.model.Asset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for {@link AssetRepository}.
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestAssetRepositoryIT extends AbstractIntegrationTest {

	@Autowired
	AssetRepository repository;


	@BeforeEach
	@AfterEach
	public void cleanup() {
		repository.deleteAll();
	}

	/**
	 * Test for {@link AssetRepository#save(Object)}
	 */
	@Test
	public void testStore() {
		Asset asset = new Asset();

		asset.setContentHash("2");
		asset.setContentType("3");
		asset.setUploadDate(OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS));
		asset.setFileSize(100L);
		asset.setFileName("test.jar");

		repository.save(asset);

		List<Asset> assets = repository.findAll();

		assertEquals(1, assets.size());

		assertEquals(asset.getContentHash(), assets.getFirst().getContentHash());
		assertEquals(asset.getContentType(), assets.getFirst().getContentType());
		assertEquals(asset.getUploadDate(), assets.getFirst().getUploadDate());
		assertEquals(asset.getFileSize(), assets.getFirst().getFileSize());
		assertEquals(asset.getFileName(), assets.getFirst().getFileName());
	}


	/**
	 * Test for {@link AssetRepository#save(Object)}
	 */
	@Test
	public void testDelete() {
		Asset asset = new Asset();

		asset.setContentHash("2");
		asset.setContentType("3");
		asset.setUploadDate(OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS));
		asset.setFileSize(100L);
		asset.setFileName("test.jar");

		repository.save(asset);

		List<Asset> assets = repository.findAll();

		assertEquals(1, assets.size());

		repository.delete(assets.getFirst());

		assertTrue(repository.findAll().isEmpty());
	}



	/**
	 * Test for {@link AssetRepository#save(Object)}
	 */
	@Test
	public void testUpdate() {
		Asset asset = new Asset();

		asset.setContentHash("2");
		asset.setContentType("3");
		asset.setUploadDate(OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS));
		asset.setFileSize(100L);
		asset.setFileName("test.jar");

		repository.save(asset);

		List<Asset> assets = repository.findAll();

		assertEquals(1, assets.size());

		OffsetDateTime dateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);

		assets.getFirst().setUploadDate(dateTime);

		assertEquals(dateTime, repository.save(assets.getFirst()).getUploadDate());
	}
}
