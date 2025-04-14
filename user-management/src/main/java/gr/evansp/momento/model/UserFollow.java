package gr.evansp.momento.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(schema = "USER_MANAGEMENT", name = "USER_FOLLOW",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"FOLLOWED_BY_ID", "FOLLOWS_ID"})})
public class UserFollow {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "ID", nullable = false, unique = true)
  UUID id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "FOLLOWS_ID", nullable = false)
  UserProfile follows;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "FOLLOWED_BY_ID", nullable = false)
  UserProfile followedBy;

  @Column(name = "CREATED_AT", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    UserFollow userFollow = (UserFollow) o;
    return id.equals(userFollow.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
