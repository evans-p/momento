package gr.evansp.momento.service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import gr.evansp.momento.model.Asset;
import org.springframework.web.multipart.MultipartFile;

/**
 * {@link Service} to manage Files.
 */
public interface AssetService {

	Optional<Asset> getAssetByPath(String path);

	Asset uploadAsset(MultipartFile file) throws IOException;

	File getPhysicalFile(Asset asset);
}
