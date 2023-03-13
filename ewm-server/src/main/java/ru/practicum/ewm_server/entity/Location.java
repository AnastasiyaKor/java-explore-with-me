package ru.practicum.ewm_server.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "locations", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "lat")
    private float lat;
    @Column(name = "lon")
    private float lon;
}
