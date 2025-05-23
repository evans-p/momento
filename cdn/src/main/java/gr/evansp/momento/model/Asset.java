package gr.evansp.momento.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents all asset metadata like photos, videos, etc.
 */
@Setter
@Getter
@Entity
@ToString
@Table(schema = "CDN", name = "ASSET")
public class Asset {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "ID", nullable = false, unique = true)
  private UUID id;

  @Column(name = "FILE_NAME", nullable = false)
  private String fileName;

  @Column(name = "CONTENT_TYPE", nullable = false)
  private String contentType;

  @Column(name = "CONTENT_HASH", nullable = false, unique = true)
  private String contentHash;

  @Column(name = "FILE_SIZE", nullable = false)
  private Long fileSize;

  @Column(name = "UPLOAD_TIME", nullable = false)
  private OffsetDateTime uploadDate = OffsetDateTime.now();

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Asset asset = (Asset) o;
    return Objects.equals(id, asset.id);
  }
}
