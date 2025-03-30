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
import gr.evansp.momento.exception.InternalServiceException;
import gr.evansp.momento.exception.ResourceNotFoundException;
import gr.evansp.momento.model.Asset;
import gr.evansp.momento.repository.AssetRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementation of {@link AssetService}.
 */
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
	public Asset getAssetByName(String name) {
		return assetRepository.findByFileName(name).orElseThrow(() -> new ResourceNotFoundException("file.not.found", new Object[]{name}));
	}

	@Validated
	@Override
	public Asset uploadAsset(@ValidFile MultipartFile file) {
		try {
			byte[] bytes = file.getBytes();

			String contentHash = DigestUtils.md5DigestAsHex(bytes);

			Optional<Asset> existingAsset = assetRepository.findByContentHash(contentHash);
			if (existingAsset.isPresent()) {
				return existingAsset.get();
			}

			String fileExtension = file.getOriginalFilename()
				                       .substring(file.getOriginalFilename().lastIndexOf("."));
			String storedFilename = UUID.randomUUID() + "-" + contentHash.substring(0, 8) + fileExtension;

			String fullPath = storageLocation + "/";

			Path targetPath = Paths.get(fullPath, storedFilename);
			Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

			return storeAssetMetadata(file, storedFilename, contentHash);
		} catch (IOException e) {
			throw new InternalServiceException(InternalServiceException.FILE_PROCESS_FAILED, null);
		}
	}

	@Transactional
	private Asset storeAssetMetadata(MultipartFile file, String storedFilename, String contentHash) {
		try {
			Asset asset = new Asset();
			asset.setFileName(storedFilename);
			asset.setContentType(file.getContentType());
			asset.setContentHash(contentHash);
			asset.setFileSize(file.getSize());
			asset.setUploadDate(OffsetDateTime.now());

			return assetRepository.save(asset);
		} catch (Exception e) {
			try {
				Files.deleteIfExists(Paths.get(storageLocation + "/", storedFilename));
			} catch (IOException ex) {
				throw new InternalServiceException(InternalServiceException.FILE_PROCESS_FAILED, null);
			}
			throw new InternalServiceException(InternalServiceException.FILE_PROCESS_FAILED, null);
		}
	}


	@Override
	public File getPhysicalFile(Asset asset) {
		return new File(storageLocation + "/" + asset.getFileName());
	}
}
