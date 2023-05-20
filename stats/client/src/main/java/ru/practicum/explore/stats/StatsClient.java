package ru.practicum.explore.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explore.client.BaseClient;

import java.util.List;


@Service
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void addHit(HitDto hitDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<HitDto> requestEntity = new HttpEntity<>(hitDto, headers);
        rest.exchange("/hit", HttpMethod.POST, requestEntity, Object.class);
    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, Boolean unique) {
        StringBuilder requestString = new StringBuilder("/stats?"
                + "start=" + start
                + "&end=" + end
                + "&unique=" + unique);

        for (String uri : uris) {
            requestString.append("&uris=").append(uri);
        }

        return get(requestString.toString());
    }

}
