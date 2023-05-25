package com.example.PlaceAR.controller;

import com.example.PlaceAR.dto.PlaceDTO;
import com.example.PlaceAR.service.GoogleMapService;
import com.example.PlaceAR.service.PlaceService;
import com.example.PlaceAR.service.RestTemplateService;
import com.google.maps.errors.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/place")
public class PlaceController {

    private final PlaceService placeService;
    private final GoogleMapService googleMapService;
    private final RestTemplateService restTemplateService;

    @Autowired
    public PlaceController(PlaceService placeService, GoogleMapService googleMapService, RestTemplateService restTemplateService) {
        this.placeService = placeService;
        this.googleMapService = googleMapService;
        this.restTemplateService = restTemplateService;
    }


    @PostMapping("/search")
    public ResponseEntity<PlaceDTO> placeSearch(@RequestParam(name = "image") MultipartFile image,
                                                @RequestParam(name = "lat") Double lat,
                                                @RequestParam(name= "lng") Double lng) throws IOException, InterruptedException, ApiException {

//        사진 저장 코드
//        File dest = new File("C:\\Users\\cyr50\\OneDrive\\바탕 화면\\사진저장" + File.separator +image.getOriginalFilename());
//        image.transferTo(dest);

        List<PlaceDTO> placeDTOList= new ArrayList<>();
        List<Double> similarity = new ArrayList<>();
        PlaceDTO result= null;


        String placename = restTemplateService.ocrRestTemplate(placeService.endcodingBase64(image));
        System.out.println(placename);
        //placename 이상할시 예외사항 작성해야함


        try {
            placeDTOList = placeService.latlngPlaceList(lat, lng);

            for (int i = 0; i < placeDTOList.size(); i++) {
                similarity.add(placeService.jacad(placeDTOList.get(i).getName(), placename));
            }

            double similarityMax = Collections.max(similarity);

            if(similarityMax != (double) 0.0){
                result = placeDTOList.get(similarity.indexOf(similarityMax));
            }

        } catch (NoSuchElementException e) {
            System.out.println("DB에 없는 정보입니다.");
        }

        try {
            if(result == null) {
                List<PlaceDTO> placelist = googleMapService.neardySearch(lat, lng);
                placeService.saveAll(placelist);

                placeDTOList = placeService.latlngPlaceList(lat, lng);

                for(int i =0; i<placeDTOList.size(); i++){
                    similarity.add(placeService.jacad(placeDTOList.get(i).getName(), placename));
                }

                double similarityMax = Collections.max(similarity);

                if(similarityMax != (double) 0.0){
                    result = placeDTOList.get(similarity.indexOf(similarityMax));
                }
            }
        }catch (NullPointerException e){
            System.out.println("무슨오류인가.");
        }


        if(result != null) {
            System.out.println("200 전송");
            return ResponseEntity.ok().body(result);
        }else{
            System.out.println("404 전송");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/placelist")
    public ResponseEntity<List<PlaceDTO>> nearbyPlaceList(@RequestParam(name = "lat") Double lat, @RequestParam(name= "lng") Double lng){

        List<PlaceDTO> result = placeService.latlngPlaceList(lat, lng);

        if(result != null){
            System.out.println("List 200 전송");
            return ResponseEntity.ok().body(result);
        }
        else{
            System.out.println("List 404 전송");
            return ResponseEntity.notFound().build();
        }
    }
}
