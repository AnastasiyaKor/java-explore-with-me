package ru.practicum.stats_service.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "App")
@Table(name = "apps", schema = "public")
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "app")
    private String app;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        App app1 = (App) o;
        return Objects.equals(app, app1.app);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app);
    }
}
