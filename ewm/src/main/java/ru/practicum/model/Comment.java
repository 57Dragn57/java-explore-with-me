package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String comment;
    @ManyToOne(optional = false)
    @JoinColumn(name = "commentator")
    private User commentator;
    @Column(name = "create_date")
    private LocalDateTime createDate;
}
