package backend_clima.dev.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainData {

    private Double temp;
    private Double feels_like;
    private Integer humidity;
    private Integer pressure;
}