package gr.evansp.momento.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import gr.evansp.momento.annotation.ValidFile;
import gr.evansp.momento.model.Asset;
import gr.evansp.momento.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Validated
@Service
public class AssetServiceImpl implements AssetService {

	@Value("${cdn.storage.location}")
	private String storageLocation;


	private final AssetRepository assetRepository;

	@Autowired
	public AssetServiceImpl(AssetRepository assetRepository) {
		this.assetRepository = assetRepository;
	}

	@Override
	public Optional<Asset> getAssetByPath(String path) {
		return assetRepository.findByPath(path);
	}

	@Override
	public Asset uploadAsset(@ValidFile MultipartFile file) throws IOException {

		byte[] bytes = file.getBytes();

		String contentHash = DigestUtils.md5DigestAsHex(bytes);

		Optional<Asset> existingAsset = assetRepository.findByContentHash(contentHash);
		if (existingAsset.isPresent()) {
			return existingAsset.get();
		}

		String originalFilename = file.getOriginalFilename();
		String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String storedFilename = UUID.randomUUID() + fileExtension;

		String relativePath = generatePath(contentHash);
		String fullPath = storageLocation + "/" + relativePath;

		// Create directories if they don't exist
		Path directory = Paths.get(fullPath).getParent();
		if (!Files.exists(directory)) {
			Files.createDirectories(directory);
		}

		// Save the file
		Path targetPath = Paths.get(fullPath, storedFilename);
		Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

		// Create and save asset metadata
		Asset asset = new Asset();
		asset.setFileName(originalFilename);
		asset.setContentType(file.getContentType());
		asset.setPath(relativePath + "/" + storedFilename);
		asset.setContentHash(contentHash);
		asset.setFileSize(file.getSize());
		asset.setUploadDate(OffsetDateTime.now());

		return assetRepository.save(asset);
	}


	private String generatePath(String contentHash) {
		return contentHash.substring(0, 2) + "/" + contentHash.substring(2, 4);
	}

	@Override
	public File getPhysicalFile(Asset asset) {
		return new File(storageLocation + "/" + asset.getPath());
	}
}
