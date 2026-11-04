package gr.evansp.momento.controller;

import gr.evansp.momento.beans.ExceptionMessage;
import gr.evansp.momento.dto.UpdateUserProfileDto;
import gr.evansp.momento.dto.UserFollowDto;
import gr.evansp.momento.dto.UserProfileDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * API contract and OpenAPI documentation for user management endpoints.
 *
 * <p>All error responses share the {@link ExceptionMessage} body, a map of message key to localized
 * message.
 */
@Tag(
    name = "User Management",
    description =
        "Endpoints for registering users, managing the logged-in user's profile, "
            + "viewing user profiles and handling follow relationships.")
public interface UserManagementControllerAPI {

  @Operation(
      summary = "Register a user",
      description =
          "Registers the user identified by the supplied JWT bearer token and returns the "
              + "newly created profile.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "User registered successfully.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserProfileDto.class))),
    @ApiResponse(
        responseCode = "400",
        description =
            "Authorization header is missing, the token is invalid, or the registration "
                + "violates a business rule (e.g. the user is already registered).",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "422",
        description = "The resulting user profile failed validation.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Unexpected internal server error.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class)))
  })
  ResponseEntity<UserProfileDto> registerUser(
      @Parameter(
              in = ParameterIn.HEADER,
              description = "Bearer JWT token in the form 'Bearer <token>'.",
              required = true,
              example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
          @RequestHeader("Authorization")
          String authorizationHeader);

  @Operation(
      summary = "Get the logged-in user",
      description = "Returns the profile of the user identified by the supplied JWT bearer token.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Profile retrieved successfully.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserProfileDto.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Authorization header is missing or the token is invalid.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No user is registered for the supplied token.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Unexpected internal server error.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class)))
  })
  ResponseEntity<UserProfileDto> getLoggedInUser(
      @Parameter(
              in = ParameterIn.HEADER,
              description = "Bearer JWT token in the form 'Bearer <token>'.",
              required = true,
              example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
          @RequestHeader("Authorization")
          String authorizationHeader);

  @Operation(
      summary = "Get the logged-in user's followers",
      description =
          "Returns a paginated list of the users who follow the logged-in user. "
              + "The logged-in user is identified by the supplied JWT bearer token.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Followers retrieved successfully. The list is empty if there are none.",
        content =
            @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserProfileDto.class)))),
    @ApiResponse(
        responseCode = "400",
        description =
            "Authorization header is missing, the token is invalid, or a pagination "
                + "parameter is not a valid number.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No user is registered for the supplied token.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Unexpected internal server error.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class)))
  })
  ResponseEntity<List<UserProfileDto>> getLoggedInUserFollowers(
      @Parameter(
              in = ParameterIn.HEADER,
              description = "Bearer JWT token in the form 'Bearer <token>'.",
              required = true,
              example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
          @RequestHeader("Authorization")
          String authorizationHeader,
      @Parameter(description = "Zero-based page index.", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Number of items per page.", example = "10")
          @RequestParam(defaultValue = "10")
          int size);

  @Operation(
      summary = "Get the users the logged-in user follows",
      description =
          "Returns a paginated list of the users that the logged-in user follows. "
              + "The logged-in user is identified by the supplied JWT bearer token.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Followed users retrieved successfully. The list is empty if there are none.",
        content =
            @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserProfileDto.class)))),
    @ApiResponse(
        responseCode = "400",
        description =
            "Authorization header is missing, the token is invalid, or a pagination "
                + "parameter is not a valid number.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No user is registered for the supplied token.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Unexpected internal server error.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class)))
  })
  ResponseEntity<List<UserProfileDto>> getLoggedInUserFollows(
      @Parameter(
              in = ParameterIn.HEADER,
              description = "Bearer JWT token in the form 'Bearer <token>'.",
              required = true,
              example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
          @RequestHeader("Authorization")
          String authorizationHeader,
      @Parameter(description = "Zero-based page index.", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Number of items per page.", example = "10")
          @RequestParam(defaultValue = "10")
          int size);

  @Operation(
      summary = "Update the logged-in user's profile",
      description =
          "Updates the profile of the user identified by the supplied JWT bearer token and "
              + "returns the updated profile.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Profile updated successfully.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserProfileDto.class))),
    @ApiResponse(
        responseCode = "400",
        description =
            "Authorization header is missing, the token is invalid, the request body is "
                + "malformed, or the media type is not supported (expects application/json).",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No user is registered for the supplied token.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "422",
        description =
            "The submitted values failed validation. The body maps each violated constraint "
                + "to its message.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Unexpected internal server error.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class)))
  })
  ResponseEntity<UserProfileDto> updateUser(
      @RequestBody(
              description = "The new profile values.",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = UpdateUserProfileDto.class)))
          @org.springframework.web.bind.annotation.RequestBody
          UpdateUserProfileDto userProfileDto,
      @Parameter(
              in = ParameterIn.HEADER,
              description = "Bearer JWT token in the form 'Bearer <token>'.",
              required = true,
              example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
          @RequestHeader("Authorization")
          String authorizationHeader);

  @Operation(
      summary = "Get a user by id",
      description =
          "Returns the public profile of the user with the given id. "
              + "No authentication required.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Profile retrieved successfully.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserProfileDto.class))),
    @ApiResponse(
        responseCode = "400",
        description = "The supplied user id is not valid.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No user exists with the given id.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Unexpected internal server error.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class)))
  })
  ResponseEntity<UserProfileDto> getUser(
      @Parameter(
              description = "Id of the user.",
              required = true,
              example = "3f2b8c1e-6a4d-4e7b-9c2a-1d5e8f7a9b30")
          @PathVariable
          String userId);

  @Operation(
      summary = "Get a user's followers",
      description =
          "Returns a paginated list of the users who follow the user with the given id. "
              + "No authentication required.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Followers retrieved successfully. The list is empty if there are none.",
        content =
            @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserProfileDto.class)))),
    @ApiResponse(
        responseCode = "400",
        description =
            "The supplied user id is not valid, or a pagination parameter is not a valid "
                + "number.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No user exists with the given id.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Unexpected internal server error.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class)))
  })
  ResponseEntity<List<UserProfileDto>> getFollowers(
      @Parameter(
              description = "Id of the user whose followers are requested.",
              required = true,
              example = "3f2b8c1e-6a4d-4e7b-9c2a-1d5e8f7a9b30")
          @PathVariable
          String userId,
      @Parameter(description = "Zero-based page index.", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Number of items per page.", example = "10")
          @RequestParam(defaultValue = "10")
          int size);

  @Operation(
      summary = "Get the users a user follows",
      description =
          "Returns a paginated list of the users that the user with the given id follows. "
              + "No authentication required.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Followed users retrieved successfully. The list is empty if there are none.",
        content =
            @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = UserProfileDto.class)))),
    @ApiResponse(
        responseCode = "400",
        description =
            "The supplied user id is not valid, or a pagination parameter is not a valid "
                + "number.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No user exists with the given id.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Unexpected internal server error.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class)))
  })
  ResponseEntity<List<UserProfileDto>> getFollows(
      @Parameter(
              description = "Id of the user whose followed users are requested.",
              required = true,
              example = "3f2b8c1e-6a4d-4e7b-9c2a-1d5e8f7a9b30")
          @PathVariable
          String userId,
      @Parameter(description = "Zero-based page index.", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Number of items per page.", example = "10")
          @RequestParam(defaultValue = "10")
          int size);

  @Operation(
      summary = "Follow a user",
      description =
          "Makes the logged-in user (identified by the JWT bearer token) follow the user with "
              + "the given id. Returns both sides of the new follow relationship.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "User followed successfully.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserFollowDto.class))),
    @ApiResponse(
        responseCode = "400",
        description =
            "Authorization header is missing, the token or user id is invalid, or the "
                + "request violates a business rule (e.g. the user is already followed or "
                + "attempts to follow themselves).",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "404",
        description = "The logged-in user or the user to follow does not exist.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Unexpected internal server error.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class)))
  })
  ResponseEntity<UserFollowDto> follow(
      @Parameter(
              description = "Id of the user to follow.",
              required = true,
              example = "3f2b8c1e-6a4d-4e7b-9c2a-1d5e8f7a9b30")
          @PathVariable
          String userId,
      @Parameter(
              in = ParameterIn.HEADER,
              description = "Bearer JWT token in the form 'Bearer <token>'.",
              required = true,
              example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
          @RequestHeader("Authorization")
          String authorizationHeader);

  @Operation(
      summary = "Unfollow a user",
      description =
          "Makes the logged-in user (identified by the JWT bearer token) stop following the "
              + "user with the given id.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "User unfollowed successfully.",
        content = @Content),
    @ApiResponse(
        responseCode = "400",
        description =
            "Authorization header is missing, the token or user id is invalid, or the "
                + "request violates a business rule (e.g. the user is not followed).",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "404",
        description = "The logged-in user or the user to unfollow does not exist.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Unexpected internal server error.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionMessage.class)))
  })
  ResponseEntity<Void> unfollow(
      @Parameter(
              description = "Id of the user to unfollow.",
              required = true,
              example = "3f2b8c1e-6a4d-4e7b-9c2a-1d5e8f7a9b30")
          @PathVariable
          String userId,
      @Parameter(
              in = ParameterIn.HEADER,
              description = "Bearer JWT token in the form 'Bearer <token>'.",
              required = true,
              example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
          @RequestHeader("Authorization")
          String authorizationHeader);
}
