package backend_clima.dev.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherData {

    private String main;
    private String description;
    private String icon;
}
