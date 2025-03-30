package gr.evansp.momento.service;

import java.io.File;


import gr.evansp.momento.annotation.ValidFile;
import org.springframework.stereotype.Service;
import gr.evansp.momento.model.Asset;
import org.springframework.web.multipart.MultipartFile;

/**
 * {@link Service} to manage Files.
 */
public interface AssetService {

	Asset getAssetByPath(String path);

	Asset uploadAsset(@ValidFile MultipartFile file);

	File getPhysicalFile(Asset asset);
}
