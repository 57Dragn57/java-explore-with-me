package ru.practicum.model;

import lombok.*;
import org.hibernate.annotations.Formula;
import ru.practicum.stats.State;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String annotation;
    @ManyToOne(optional = false)
    @JoinColumn(name = "category", nullable = false)
    private Category category;
    @Formula("(SELECT COUNT(*) FROM requests WHERE requests.event_id = id AND requests.status = 'CONFIRMED')")
    private int confirmedRequests;
    @Column(name = "create_date")
    private LocalDateTime createdOn;
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne(optional = false)
    @JoinColumn(name = "initiator", nullable = false)
    private User initiator;
    private float lat;
    private float lon;
    private boolean paid;
    @Column(name = "participant_limit")
    private int participantLimit;
    @Column(name = "published_date")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private State state;
    private String title;
    @ManyToMany
    @JoinTable(name = "comments_events",
            joinColumns = @JoinColumn(name = "event"),
            inverseJoinColumns = @JoinColumn(name = "comment"))
    private Set<Comment> comments;
}
