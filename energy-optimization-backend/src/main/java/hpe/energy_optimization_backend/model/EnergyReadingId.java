package hpe.energy_optimization_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyReadingId implements Serializable {

    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "timestamp")
    private Instant timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnergyReadingId that = (EnergyReadingId) o;
        return Objects.equals(deviceId, that.deviceId) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, timestamp);
    }
}