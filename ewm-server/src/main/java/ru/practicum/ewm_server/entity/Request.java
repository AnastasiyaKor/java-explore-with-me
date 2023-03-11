package ru.practicum.ewm_server.entity;

import lombok.*;
import ru.practicum.ewm_server.enums.RequestStatusEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "created")
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event")
    private Event event;//id
    @ManyToOne
    @JoinColumn(name = "requester")
    private User requester;//id
    @Enumerated(EnumType.STRING)
    private RequestStatusEnum status;

    public Request(LocalDateTime created, Event event, User requester, RequestStatusEnum status) {
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.status = status;
    }
}
