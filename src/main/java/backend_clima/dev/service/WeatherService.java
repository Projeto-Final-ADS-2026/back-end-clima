package backend_clima.dev.service;

import backend_clima.dev.dto.WeatherApiResponse;
import backend_clima.dev.dto.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestTemplate restTemplate;
    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherResponse getWeatherByCity(String cityName) {

        // 2. Montagem da URL com os parâmetros dinâmicos (cidade e unidade em Celsius)
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey + "&units=metric&lang=pt_br";

        // 3. Chamada HTTP para a API externa
        try {

            WeatherApiResponse apiResponse =
                    restTemplate.getForObject(url, WeatherApiResponse.class);

            WeatherResponse response = new WeatherResponse();

            if (apiResponse != null) {

                response.setCityName(apiResponse.getName());

                if (apiResponse.getSys() != null) {
                    response.setCountry(apiResponse.getSys().getCountry());
                }

                if (apiResponse.getMain() != null) {
                    response.setTemp(apiResponse.getMain().getTemp());
                    response.setFeelsLike(apiResponse.getMain().getFeels_like());
                    response.setHumidity(apiResponse.getMain().getHumidity());
                    response.setPressure(apiResponse.getMain().getPressure());
                }

                if (apiResponse.getWeather() != null
                        && !apiResponse.getWeather().isEmpty()) {

                    response.setDescription(
                            apiResponse.getWeather().get(0).getDescription()
                    );
                }

                if (apiResponse.getWind() != null) {
                    response.setWindSpeed(apiResponse.getWind().getSpeed());
                }

                response.setVisibility(
                        Double.valueOf(apiResponse.getVisibility())
                );
            }

            return response;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao buscar clima para a cidade: " + cityName, e
            );
        }
    }
}
