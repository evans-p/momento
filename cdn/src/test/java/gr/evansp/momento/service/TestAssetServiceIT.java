package gr.evansp.momento.service;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import gr.evansp.momento.AbstractIntegrationTest;
import gr.evansp.momento.exception.ResourceNotFoundException;
import gr.evansp.momento.model.Asset;
import gr.evansp.momento.repository.AssetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Integration tests for {@link AssetService}.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TestAssetServiceIT extends AbstractIntegrationTest {

	@Value("${cdn.storage.location}")
	private String storageLocation;

	@Autowired
	AssetService service;

	@Autowired
	AssetRepository repository;

	@AfterEach
	@BeforeEach
	public void cleanup() {
		repository.deleteAll();
	}

	/**
	 * Test for {@link AssetService#uploadAsset(MultipartFile)}.
	 *
	 * @throws IOException
	 * 		IOException
	 */
	@Test
	public void testUploadAsset_ok() throws IOException {
		MultipartFile file = new MockMultipartFile("file", "Yosuke.jpg", "image/jpeg", JPG_IMAGE);

		Asset asset = service.uploadAsset(file);

		assertNotNull(asset);
		assertNotNull(asset.getId());

		Files.deleteIfExists(Path.of(storageLocation + "/" + asset.getFileName()));
	}

	/**
	 * Test for {@link AssetService#uploadAsset(MultipartFile)}.
	 *
	 * @throws IOException
	 * 		IOException
	 */
	@Test
	public void testUploadAsset_alreadyExists() throws IOException {
		MultipartFile file = new MockMultipartFile("file", "Yosuke.jpg", "image/jpeg", JPG_IMAGE);

		Asset asset = service.uploadAsset(file);
		Asset duplicateAsset = service.uploadAsset(file);

		assertNotNull(asset);
		assertNotNull(asset.getId());
		assertEquals(asset, duplicateAsset);

		Files.deleteIfExists(Path.of(storageLocation + "/" + asset.getFileName()));
	}

	/**
	 * Test for {@link AssetService#getFileByName(String)}.
	 */
	@Test
	public void testGetFileByName_noMetadata() {
		ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> service.getFileByName("10283bb6-8664-474d-9240-2036b59b4ece-f1e8af53.png"));
		assertEquals(ResourceNotFoundException.FILE_NOT_FOUND, e.getMessage());
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

		ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> service.getFileByName(asset.getFileName()));
		assertEquals(ResourceNotFoundException.FILE_NOT_FOUND, e.getMessage());
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

		File result = service.getFileByName(asset.getFileName());

		assertNotNull(result);
		assertArrayEquals(file.getBytes(), Files.readAllBytes(result.toPath()));

		Files.deleteIfExists(Path.of(storageLocation + "/" + asset.getFileName()));
	}
}