package gr.evansp.momento.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Entity
@ToString
@Table(schema = "POST_MANAGEMENT", name = "POST")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "ID", nullable = false, unique = true)
  private UUID id;

  @Column(name = "IMAGE_URL", nullable = false)
  private String imageUrl;

  @Column(name = "OWNER_ID", nullable = false)
  private UUID ownerId;

  @Column(name = "LATITUDE")
  private float latitude;

  @Column(name = "LONGITUDE")
  float longitude;

  @Column(name = "PLACE_NAME")
  String placeName;

  @Column(name = "PLACE_ID")
  String placeId;

  @Column(name = "LIKES_COUNT", nullable = false)
  Long likesCount = 0L;

  @Column(name = "COMMENTS_COUNT", nullable = false)
  Long commentsCount = 0L;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  List<Like> likes = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  List<Comment> comments = new ArrayList<>();

  @Column(name = "CREATED_AT", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Column(name = "UPDATED_AT", nullable = false)
  private OffsetDateTime updatedAt = OffsetDateTime.now();

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    Post post = (Post) o;
    return id.equals(post.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
