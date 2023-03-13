package ru.practicum.ewm_server.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "compilations", schema = "public")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "compilation_events",
            joinColumns = @JoinColumn(
                    name = "compilation_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "event_id",
                    referencedColumnName = "id"
            )
    )
    @ToString.Exclude
    private Set<Event> events;
    @Column(name = "pinned")
    private Boolean pinned;
    @Column(name = "title", length = 120)
    private String title;

    public Compilation(Set<Event> events, Boolean pinned, String title) {
        this.events = events;
        this.pinned = pinned;
        this.title = title;
    }
}
