package gr.evansp.momento.service;

import gr.evansp.momento.model.Asset;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * {@link Service} to manage {@link Asset} Metadata.
 */
public interface AssetMetadataService {

  /**
   * Stores {@link Asset} metadata
   * @param file file
   * @param storedFilename storedFilename
   * @param contentHash contentHash
   * @return {@link Asset}
   */
  Asset storeAssetMetadata(MultipartFile file, String storedFilename, String contentHash);
}
