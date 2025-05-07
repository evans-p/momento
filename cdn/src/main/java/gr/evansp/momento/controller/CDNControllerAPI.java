package gr.evansp.momento.controller;

import gr.evansp.momento.bean.ExceptionMessage;
import gr.evansp.momento.dto.AssetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("TextBlockMigration")
@Tag(name = "Content Management API", description = "Handles content upload and fetching.")
interface CDNControllerAPI {

  @Operation(summary = "Uploads new asset. Supports 'png' and 'jpg/jpeg' files.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "File uploaded successfully.",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = AssetDto.class),
                  examples =
                      @ExampleObject(
                          value =
                              "{\n"
                                  + "\"fileName\": \"fb2ae93f-4c57-45dd-adce-8a9194b62254-fcdcc20a.png\",\n"
                                  + "\"contentType\": \"image/png\",\n"
                                  + "\"fileSize\": 45366,\n"
                                  + "\"uploadDate\": \"2025-05-06T22:55:49.997508047\"\n"
                                  + "}"))
            }),
        @ApiResponse(
            responseCode = "422",
            description = "Invalid file provided.",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ExceptionMessage.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\n"
                                    + "\"messages\": {\n"
                                    + "\"invalid.file.content\": \"Invalid file content or mismatch between file content, suffix and content type.\"\n"
                                    + "}\n"
                                    + "}"))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error.",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ExceptionMessage.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\n"
                                    + "\"messages\": {\n"
                                    + "\"internal.server.error\": \"Internal server Error.\"\n"
                                    + "}\n"
                                    + "}")))
      })
  @Parameters(
      value =
          @Parameter(
              name = "file",
              description = "File to be uploaded. Supports 'png' and 'jpg/jpeg' files, up to 5 MB.",
              required = true,
              content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE),
              example = "(Binary file content not displayable here)"))
  ResponseEntity<AssetDto> upload(
      @RequestParam("file")
          @RequestBody(
              description = "File to be uploaded. Supports 'png' and 'jpg/jpeg' files, up to 5 MB.",
              required = true,
              content =
                  @Content(
                      schema = @Schema(implementation = MultipartFile.class),
                      mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
          MultipartFile file);

  ResponseEntity<FileSystemResource> getFile(@PathVariable String fileName);
}
