package gr.evansp.momento.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(schema = "USER_MANAGEMENT", name = "USER_PROFILE")
public class UserProfile {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "ID", nullable = false, unique = true)
  private UUID id;

  @Column(name = "FIRST_NAME", nullable = false)
  private String firstName;

  @Column(name = "LAST_NAME", nullable = false)
  private String lastName;

  @Column(name = "EMAIL", nullable = false, unique = true)
  private String email;

  @Column(name = "CREATED_AT", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Column(name = "UPDATED_AT", nullable = false)
  private OffsetDateTime updatedAt = OffsetDateTime.now();

  @Column(name = "PROFILE_PICTURE_URL")
  private String profilePictureUrl;

  @Column(name = "AUTHENTICATION_PROVIDER_ID", nullable = false, unique = true)
  private String authenticationProviderId;

  @Column(name = "FOLLOWS_COUNT", nullable = false)
  private Long followsCount = 0L;

  @Column(name = "FOLLOWED_BY_COUNT", nullable = false)
  private Long followedByCount = 0L;

  @OneToMany(mappedBy = "follows", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<UserFollow> followsList = new LinkedList<>();

  @OneToMany(mappedBy = "followedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<UserFollow> followedByList = new LinkedList<>();

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    UserProfile userProfile = (UserProfile) o;
    return id.equals(userProfile.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
