package backend_clima.dev.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "weather_history", indexes = @Index(name = "idx_history_city_date", columnList = "city_id, searchedAt"))
public class WeatherHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    private Double temperature;
    private Double feelsLike;
    private Integer humidity;
    private Integer pressure;
    private Double windSpeed;
    private Double visibility;

    @Column(length = 200)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime searchedAt;

    @PrePersist
    void onCreate() {
        this.searchedAt = LocalDateTime.now();
    }
}
