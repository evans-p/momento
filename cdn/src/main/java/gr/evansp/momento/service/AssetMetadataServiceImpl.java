package gr.evansp.momento.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import gr.evansp.momento.exception.InternalServiceException;
import gr.evansp.momento.model.Asset;
import gr.evansp.momento.repository.AssetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static gr.evansp.momento.constants.ExceptionConstants.FILE_PROCESS_FAILED;

@Slf4j
@Service
public class AssetMetadataServiceImpl implements AssetMetadataService {

	@Value("${cdn.storage.location}")
	private String storageLocation;

	private final AssetRepository assetRepository;

	@Autowired
	public AssetMetadataServiceImpl(AssetRepository assetRepository) {
		this.assetRepository = assetRepository;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	@Override
	public Asset storeAssetMetadata(MultipartFile file, String storedFilename, String contentHash) {
		Asset asset = new Asset();
		asset.setFileName(storedFilename);
		asset.setContentType(file.getContentType());
		asset.setContentHash(contentHash);
		asset.setFileSize(file.getSize());

		try {
			return assetRepository.save(asset);
		} catch (Exception e) {
			log.warn("uploadAsset: failed saving asset metadata: {}.", asset);
			try {
				Files.deleteIfExists(Paths.get(storageLocation + "/", storedFilename));
			} catch (IOException ex) {
				throw new InternalServiceException(FILE_PROCESS_FAILED, null);
			}
			throw new InternalServiceException(FILE_PROCESS_FAILED, null);
		}
	}
}
