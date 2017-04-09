package com.dalgim.example.sb.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

/**
 * Created by dalgim on 09.04.2017.
 */
@Service
@RequiredArgsConstructor
public class BookClientImpl implements BookClient {

    private final RestTemplate restTemplate;

    @Override
    public void getAllBook() {
        ResponseEntity<Collection> forEntity = restTemplate.getForEntity("https://localhost:8443/api/book/all", Collection.class);
        System.out.println();
    }
}
