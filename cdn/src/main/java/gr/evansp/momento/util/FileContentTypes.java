package gr.evansp.momento.util;

import gr.evansp.momento.annotation.ValidFileName;
import java.util.Set;
import org.antlr.v4.runtime.misc.Triple;
import org.springframework.http.MediaType;

/**
 *
 */
public class FileContentTypes {

  private FileContentTypes() {
    // EMPTY
  }

  private static final Set<Triple<String, Set<String>, MediaType>> VALID_MEDIA = initializeMedia();

  private static Set<Triple<String, Set<String>, MediaType>> initializeMedia() {
    return Set.of(
        new Triple<>(MediaType.IMAGE_PNG_VALUE, Set.of("png"), MediaType.IMAGE_PNG),
        new Triple<>(MediaType.IMAGE_JPEG_VALUE, Set.of("jpg", "jpeg"), MediaType.IMAGE_JPEG));
  }

  public static Set<Triple<String, Set<String>, MediaType>> getValidMedia() {
    return VALID_MEDIA;
  }

  /**
   * Assumes that the 'fileName' parameter has already been validated using {@link ValidFileName}.
   * Maps the fileName extension to the appropriate {@link MediaType}.
   * Defaults to {@link MediaType#IMAGE_PNG}.
   *
   * @param fileName
   * 		example 'lucky.png'
   * @return {@link MediaType}.
   */
  public static MediaType getContentType(String fileName) {
    String extension = fileName.substring(fileName.lastIndexOf("."));

    return VALID_MEDIA.stream()
        .filter(t -> t.b.contains(extension))
        .map(t -> t.c)
        .findFirst()
        .orElse(MediaType.IMAGE_PNG);
  }

  /**
   * Returns {@link MediaType} of a byte array, by checking the content.
   *
   * @param fileData
   * 		fileData
   *
   * @return {@link MediaType} extension or null.
   */
  public static String getContentTypeAsString(byte[] fileData) {
    if (fileData.length <= 4) {
      return null;
    }

    if (fileData[0] == (byte) 0xFF && fileData[1] == (byte) 0xD8 && fileData[2] == (byte) 0xFF) {
      return MediaType.IMAGE_JPEG_VALUE;
    } else if (fileData[0] == (byte) 0x89
        && fileData[1] == (byte) 0x50
        && fileData[2] == (byte) 0x4E
        && fileData[3] == (byte) 0x47) {
      return MediaType.IMAGE_PNG_VALUE;
    }
    return null;
  }
}
