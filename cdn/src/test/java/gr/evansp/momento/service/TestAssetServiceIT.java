package gr.evansp.momento.service;


import gr.evansp.momento.AbstractIntegrationTest;
import gr.evansp.momento.model.Asset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for {@link AssetService}.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TestAssetServiceIT extends AbstractIntegrationTest {

	@Autowired
	AssetService service;

	@Test
	public void testUploadAsset() {
		MultipartFile file = new MockMultipartFile("file", "Yosuke.jpg", "image/jpeg", JPG_IMAGE);

		Asset asset = service.uploadAsset(file);

		assertNotNull(asset);
		assertNotNull(asset.getId());
	}
}