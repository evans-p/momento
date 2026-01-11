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
@Table(schema = "POST_MANAGEMENT", name = "COMMENT")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, unique = true)
    UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "POST_ID", nullable = false)
    Post post;

    @Column(name = "USER_ID", nullable = false)
    UUID userId;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "CREATED_AT", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;
        return id.equals(comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
