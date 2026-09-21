package backend_clima.dev.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse {

    private String cityName;
    private String country;
    private Double temp;
    private String description;
    private Double feelsLike;
    private Integer humidity;
    private Double windSpeed;
    private Integer pressure;
    private Double visibility;
}
