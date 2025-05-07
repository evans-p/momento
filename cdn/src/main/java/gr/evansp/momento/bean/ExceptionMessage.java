package gr.evansp.momento.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

/**
 * Record to be returned when an {@link Exception} occurs.
 */
@Schema(
    name = "ExceptionMessage",
    description = "Error response containing validation or processing error messages")
public record ExceptionMessage(
    @Schema(
            description =
                "Map of error keys to error messages providing details about what went wrong",
            example =
                "{\n"
                    + "\"messages\": {\n"
                    + "\"invalid.file.content\": \"Invalid file content or mismatch between file content, suffix and content type.\"\n"
                    + "}\n"
                    + "}")
        Map<String, String> messages) {}
