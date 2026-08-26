package gr.evansp.momento.dto;

import java.time.LocalDateTime;

/**
 * Dto for Asset
 */
public record AssetDto(
        String fileName,
        String contentType,
        LocalDateTime uploadDate) {
}
