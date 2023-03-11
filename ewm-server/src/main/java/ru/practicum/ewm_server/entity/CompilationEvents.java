package ru.practicum.ewm_server.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "compilation_events", schema = "public")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompilationEvents implements Serializable {
    @Id
    @Column(name = "compilation_id")
    private int compId;
    @Id
    @Column(name = "event_id")
    private int eventId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompilationEvents that = (CompilationEvents) o;
        return compId == that.compId && eventId == that.eventId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(compId, eventId);
    }
}
