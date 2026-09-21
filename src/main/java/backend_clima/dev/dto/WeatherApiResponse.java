package backend_clima.dev.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherApiResponse {

    private String name;
    private MainData main;
    private List<WeatherData> weather;
    private WindData wind;
    private Integer visibility;
    private SysData sys;
}