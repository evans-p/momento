package gr.evansp.momento.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gr.evansp.momento.AbstractIntegrationTest;
import gr.evansp.momento.bean.FileWithContentType;
import gr.evansp.momento.constants.ExceptionConstants;
import gr.evansp.momento.exception.ResourceNotFoundException;
import gr.evansp.momento.model.Asset;
import jakarta.validation.ConstraintViolationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

/**
 * Integration tests for {@link AssetService}.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TestAssetServiceIT extends AbstractIntegrationTest {

  @Autowired AssetService service;

  /**
   * Test for {@link AssetService#uploadAsset(MultipartFile)}.
   */
  @Test
  public void testUploadAsset_ok() {
    MultipartFile file = new MockMultipartFile("file", "Yosuke.jpg", "image/jpeg", JPG_IMAGE);

    Asset asset = service.uploadAsset(file);

    assertNotNull(asset);
    assertNotNull(asset.getId());
  }

  /**
   * Test for {@link AssetService#uploadAsset(MultipartFile)}.
   */
  @Test
  public void testUploadAsset_alreadyExists() {
    MultipartFile file = new MockMultipartFile("file", "Yosuke.jpg", "image/jpeg", JPG_IMAGE);

    Asset asset = service.uploadAsset(file);
    Asset duplicateAsset = service.uploadAsset(file);

    assertNotNull(asset);
    assertNotNull(asset.getId());
    assertEquals(asset, duplicateAsset);
  }

  /**
   * Test for {@link AssetService#uploadAsset(MultipartFile)}.
   */
  @Test
  public void testUploadAsset_invalidFile() {
    MultipartFile file = new MockMultipartFile("file", "Yosuke.png", "image/jpeg", JPG_IMAGE);

    ConstraintViolationException e =
        assertThrows(ConstraintViolationException.class, () -> service.uploadAsset(file));

    assertEquals(1, e.getConstraintViolations().size());

    assertEquals("uploadAsset.file: Content type and file suffix do not match.", e.getMessage());
  }

  /**
   * Test for {@link AssetService#getFileByName(String)}.
   */
  @Test
  public void testGetFileByName_noMetadata() {
    ResourceNotFoundException e =
        assertThrows(
            ResourceNotFoundException.class,
            () -> service.getFileByName("10283bb6-8664-474d-9240-2036b59b4ece-f1e8af53.png"));
    assertEquals(ExceptionConstants.FILE_NOT_FOUND, e.getMessage());
  }

  /**
   * Test for {@link AssetService#getFileByName(String)}.
   *
   * @throws IOException
   * 		IOException
   */
  @Test
  public void testGetFileByName_noFile() throws IOException {
    MultipartFile file = new MockMultipartFile("file", "Yosuke.jpg", "image/jpeg", JPG_IMAGE);
    Asset asset = service.uploadAsset(file);
    Files.deleteIfExists(Path.of(storageLocation + "/" + asset.getFileName()));

    ResourceNotFoundException e =
        assertThrows(
            ResourceNotFoundException.class, () -> service.getFileByName(asset.getFileName()));
    assertEquals(ExceptionConstants.FILE_NOT_FOUND, e.getMessage());
  }

  /**
   * Test for {@link AssetService#getFileByName(String)}.
   *
   * @throws IOException
   * 		IOException
   */
  @Test
  public void testGetFileByName_ok() throws IOException {
    MultipartFile file = new MockMultipartFile("file", "Yosuke.jpg", "image/jpeg", JPG_IMAGE);
    Asset asset = service.uploadAsset(file);

    FileWithContentType result = service.getFileByName(asset.getFileName());

    assertNotNull(result);
    assertArrayEquals(file.getBytes(), Files.readAllBytes(result.file().toPath()));
  }
}
