package ru.practicum.ewm_server.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "name")
    private String name;
}
