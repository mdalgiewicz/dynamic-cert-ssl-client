package com.dalgim.example.sb.client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Mateusz Dalgiewicz on 09.04.2017.
 */
@Service
public class RestApiClientTest implements RestApiClient {

    private final RestTemplate restTemplate;
    private final String producerAddress;

    public RestApiClientTest(RestTemplate restTemplate,
                             @Value("${client.producer-address}") String producerAddress) {
        this.restTemplate = restTemplate;
        this.producerAddress = producerAddress;
    }

    @Override
    public void connect() {
        ResponseEntity<Void> response = restTemplate.getForEntity(producerAddress, Void.class);
        System.out.println(response);
    }
}
