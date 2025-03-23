package gr.evansp.momento.it.repository;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import gr.evansp.momento.it.AbstractIntegrationTest;
import gr.evansp.momento.model.Asset;
import gr.evansp.momento.repository.AssetRepository;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for {@link AssetRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAssetRepository extends AbstractIntegrationTest {

	@Autowired
	AssetRepository repository;


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

		asset.setPath("1");
		asset.setContentHash("2");
		asset.setContentType("3");
		asset.setUploadDate(OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS));
		asset.setFileSize(100L);
		asset.setFileName("test.jar");

		repository.save(asset);

		List<Asset> assets = repository.findAll();

		assertEquals(1, assets.size());

		assertEquals(asset.getPath(), assets.getFirst().getPath());
		assertEquals(asset.getContentHash(), assets.getFirst().getContentHash());
		assertEquals(asset.getContentType(), assets.getFirst().getContentType());
		assertEquals(asset.getUploadDate(), assets.getFirst().getUploadDate());
		assertEquals(asset.getFileSize(), assets.getFirst().getFileSize());
		assertEquals(asset.getFileName(), assets.getFirst().getFileName());
	}
}
