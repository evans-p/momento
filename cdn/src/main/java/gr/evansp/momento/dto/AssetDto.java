package gr.evansp.momento.dto;

import gr.evansp.momento.model.Asset;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import org.springframework.http.MediaType;

/**
 * Dto for {@link Asset}.
 */
@Schema(name = "Asset", description = "Represents metadata about an uploaded file asset.")
public record AssetDto(
    @Schema(
            description = "Original name of the uploaded file.",
            example = "fb2ae93f-4c57-45dd-adce-8a9194b62254-fcdcc20a.png")
        String fileName,
    @Schema(description = "MIME type of the file.", example = MediaType.IMAGE_PNG_VALUE)
        String contentType,
    @Schema(description = "Size of the file in bytes.", example = "1048576") Long fileSize,
    @Schema(
            description = "Date and time when the file was uploaded.",
            example = "2025-05-07T15:30:45")
        LocalDateTime uploadDate) {

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
