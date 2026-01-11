package gr.evansp.momento.dto;

/**
 * Data Transfer Object representing geographical location information for a post.
 * <p>
 * This record encapsulates both coordinate-based location data (latitude/longitude)
 * and human-readable location information (place name and identifier).
 * </p>
 *
 * @param latitude  The latitude coordinate in decimal degrees. Valid range: -90.0 to 90.0.
 *                  Positive values represent northern hemisphere, negative values represent southern hemisphere.
 * @param longitude The longitude coordinate in decimal degrees. Valid range: -180.0 to 180.0.
 *                  Positive values represent eastern hemisphere, negative values represent western hemisphere.
 * @param placeName The human-readable name of the location (e.g., "Central Park, New York", "Eiffel Tower, Paris").
 *                  Can be {@code null} if only coordinates are provided.
 * @param placeId   A unique identifier for the location, typically referencing an external places database
 *                  or service (e.g., Google Places ID, Foursquare venue ID). Can be {@code null} if not available.
 *
 * @author Momento Development Team
 * @since 1.0
 */
public record GeolocationDto(Float latitude, Float longitude, String placeName, String placeId) {}
