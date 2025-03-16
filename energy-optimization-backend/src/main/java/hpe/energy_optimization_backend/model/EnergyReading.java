package hpe.energy_optimization_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "energy_readings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyReading implements Serializable {

    @EmbeddedId
    private EnergyReadingId id;

    @ManyToOne
    @MapsId("deviceId")
    @JoinColumn(name = "device_id", foreignKey = @ForeignKey(name = "fk_energy_readings_devices"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Device device;

    @Column(name = "voltage", nullable = false)
    private Double voltage;

    @Column(name = "current", nullable = false)
    private Double current;

    @Column(name = "power", nullable = false)
    private Double power;

    @Column(name = "energy", nullable = false)
    private Double energy;

    @Column(name = "frequency", nullable = false)
    private Double frequency;

    @Column(name = "power_factor", nullable = false)
    private Double powerFactor;
}