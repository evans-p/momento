package gr.evansp.momento.bean;

import java.io.File;
import org.springframework.http.MediaType;

/**
 * FileWithMediaType
 *
 * @param file {@link File}
 * @param contentType {@link MediaType}
 */
public record FileWithContentType(File file, MediaType contentType){}