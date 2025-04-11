package gr.evansp.momento.dto;

import gr.evansp.momento.model.Asset;
import java.time.LocalDateTime;

/**
 * Dto for {@link Asset}.
 *
 * @param fileName
 * 		fileName
 * @param contentType
 * 		contentType
 * @param fileSize
 * 		fileSize
 * @param uploadDate
 * 		uploadDate
 */
public record AssetDto(
    String fileName, String contentType, Long fileSize, LocalDateTime uploadDate) {

  /**
   * Static transformation method from {@link Asset} to {@link AssetDto}.
   *
   * @param asset
   *        {@link Asset}.
   * @return {@link AssetDto}.
   */
  public static AssetDto of(Asset asset) {
    return new AssetDto(
        asset.getFileName(),
        asset.getContentType(),
        asset.getFileSize(),
        asset.getUploadDate().toLocalDateTime());
  }
}
