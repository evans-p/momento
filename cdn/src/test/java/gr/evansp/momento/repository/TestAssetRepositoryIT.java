package gr.evansp.momento.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gr.evansp.momento.AbstractIntegrationTest;
import gr.evansp.momento.model.Asset;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Integration tests for {@link AssetRepository}.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestAssetRepositoryIT extends AbstractIntegrationTest {

  @Autowired AssetRepository repository;

  /**
   * Test for {@link AssetRepository#save(Object)}
   */
  @Test
  public void testStore() {
    Asset asset = createSampleAsset();

    repository.save(asset);

    List<Asset> assets = repository.findAll();

    assertEquals(1, assets.size());

    assertEquals(asset.getContentHash(), assets.getFirst().getContentHash());
    assertEquals(asset.getContentType(), assets.getFirst().getContentType());
    assertEquals(asset.getUploadDate(), assets.getFirst().getUploadDate());
    assertEquals(asset.getFileSize(), assets.getFirst().getFileSize());
    assertEquals(asset.getFileName(), assets.getFirst().getFileName());
  }

  private Asset createSampleAsset() {
    Asset asset = new Asset();

    asset.setContentHash("2");
    asset.setContentType("3");
    asset.setUploadDate(OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS));
    asset.setFileSize(100L);
    asset.setFileName("test.jar");

    return asset;
  }

  /**
   * Test for {@link AssetRepository#save(Object)}
   */
  @Test
  public void testDelete() {
    repository.save(createSampleAsset());

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
    repository.save(createSampleAsset());

    List<Asset> assets = repository.findAll();

    assertEquals(1, assets.size());

    OffsetDateTime dateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);

    assets.getFirst().setUploadDate(dateTime);

    assertEquals(dateTime, repository.save(assets.getFirst()).getUploadDate());
  }
}
