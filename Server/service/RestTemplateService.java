package com.example.PlaceAR.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;

@Service
public class RestTemplateService {

    //다른 server로 요청하는 서비스

    public String ocrRestTemplate(String base64){

        RestTemplate restTemplate = new RestTemplate();

        //URI를 빌드
        URI uri = UriComponentsBuilder
                .fromUriString("http://hsj3925.iptime.org:9090")
                .path("/ocr")
                .encode(Charset.defaultCharset())
                .build()
                .toUri();

        //파라미터 첫번째는 요청 URI 이며 , 2번째는 request, 3번쨰는 반환받을 타입
        ResponseEntity<String> response = restTemplate.postForEntity(uri, base64, String.class);

        return response.getBody();
    }
}

