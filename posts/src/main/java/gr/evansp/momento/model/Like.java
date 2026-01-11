package gr.evansp.momento.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@Entity
@ToString
@Table(schema = "POST_MANAGEMENT", name = "LIKE", uniqueConstraints = {@UniqueConstraint(columnNames = {"POST_ID", "USER_ID"})})
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, unique = true)
    UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "POST_ID", nullable = false)
    Post post;

    @Column(name = "USER_ID", nullable = false)
    UUID userId;

    @Column(name = "CREATED_AT", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Like like = (Like) o;
        return id.equals(like.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
