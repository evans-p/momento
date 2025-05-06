package gr.evansp.momento.service;

import static gr.evansp.momento.constants.ExceptionConstants.*;

import gr.evansp.momento.annotation.ValidFile;
import gr.evansp.momento.annotation.ValidFileName;
import gr.evansp.momento.bean.FileWithContentType;
import gr.evansp.momento.exception.InternalServiceException;
import gr.evansp.momento.exception.ResourceNotFoundException;
import gr.evansp.momento.model.Asset;
import gr.evansp.momento.repository.AssetRepository;
import gr.evansp.momento.util.FileContentTypes;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementation of {@link AssetService}.
 */
@Slf4j
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
  public Asset uploadAsset(@ValidFile MultipartFile file) {
    log.info("uploadAsset: uploading: {}", file.getOriginalFilename());

    try {
      byte[] bytes = file.getBytes();

      String contentHash = DigestUtils.md5DigestAsHex(bytes);

      Optional<Asset> existingAsset = assetRepository.findByContentHash(contentHash);
      if (existingAsset.isPresent()) {
        return existingAsset.get();
      }

      String fileExtension =
          file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
      String storedFilename = UUID.randomUUID() + "-" + contentHash.substring(0, 8) + fileExtension;

      String fullPath = storageLocation + "/";

      Path targetPath = Paths.get(fullPath, storedFilename);
      Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

      return storeAssetMetadata(file, storedFilename, contentHash);
    } catch (IOException e) {
      log.warn("uploadAsset: failed storing file: {}.", file);
      throw new InternalServiceException(FILE_PROCESS_FAILED, null);
    }
  }

  @Transactional
  private Asset storeAssetMetadata(MultipartFile file, String storedFilename, String contentHash) {
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

  @Override
  public FileWithContentType getFileByName(@ValidFileName String name) {
    Optional<Asset> result = assetRepository.findByFileName(name);

    if (result.isEmpty()) {
      log.info("getFileByName: File metadata don't exist: {}.", name);
      throw new ResourceNotFoundException(FILE_NOT_FOUND, new Object[] {name});
    }
    Asset asset = result.get();

    File file = new File(storageLocation + "/" + asset.getFileName());

    if (!file.exists()) {
      log.warn("getFileByName: File does not exist: {}.", name);
      throw new ResourceNotFoundException(FILE_NOT_FOUND, new Object[] {name});
    }

    return new FileWithContentType(file, FileContentTypes.getContentType(name));
  }
}
