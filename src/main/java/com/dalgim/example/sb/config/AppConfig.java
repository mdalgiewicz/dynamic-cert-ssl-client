package com.dalgim.example.sb.config;

import com.dalgim.example.sb.ssl.api.SSLContextService;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Mateusz Dalgiewicz on 09.04.2017.
 */
@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final SSLContextService sslContextService;

    @Bean
    public RestTemplate restTemplate() throws Exception {
        return new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() throws Exception {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    private HttpClient httpClient() {
        return HttpClients.custom()
                .setSSLContext(sslContextService.initSSLContext())
                .build();
    }
}
