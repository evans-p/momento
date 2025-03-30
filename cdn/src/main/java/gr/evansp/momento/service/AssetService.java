package gr.evansp.momento.service;

import java.io.File;

import gr.evansp.momento.annotation.ValidFile;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import gr.evansp.momento.model.Asset;
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
	 *
	 * @param name
	 * @return
	 */
	File getFileByName(@NotEmpty(message = "{faulty.file.name}") String name);
}
