package gr.evansp.momento.service;

import gr.evansp.momento.annotation.ValidFile;
import gr.evansp.momento.annotation.ValidFileName;
import gr.evansp.momento.bean.FileWithContentType;
import gr.evansp.momento.model.Asset;
import java.io.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * {@link Service} to manage Files.
 */
public interface AssetService {

  /**
   * Uploads {@link MultipartFile} to system.
   *
   * @param file
   *        {@link MultipartFile}
   * @return {@link Asset}
   */
  Asset uploadAsset(@ValidFile MultipartFile file);

  /**
   * Returns the file from the filesystem.
   *
   * @param name
   * 		file Name
   * @return {@link File}
   */
  FileWithContentType getFileByName(@ValidFileName String name);
}
