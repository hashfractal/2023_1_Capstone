package com.example.PlaceAR.service;


import com.example.PlaceAR.dto.PlaceDTO;
import com.example.PlaceAR.repository.PlaceRepository;
import com.google.maps.errors.ApiException;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import scala.collection.Seq;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static java.lang.Math.*;
import static org.openkoreantext.processor.OpenKoreanTextProcessorJava.*;

@Service
public class PlaceService {


    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;

    }

    @Getter
    @Setter
    class LngLatRange {
        Double minLat;
        Double maxLat;
        Double minLng;
        Double maxLng;
    }

    public PlaceDTO findPlace(String placename){

        PlaceDTO placeDTO = placeRepository.findByName(placename); //DB검색

        return placeDTO;
    }

    public void saveAll(List<PlaceDTO> DTOList){
            placeRepository.saveAll(DTOList);
    }

    public List<PlaceDTO> latlngPlaceList(Double lat, Double lng){

        LngLatRange lngLatRange = rangeCalculation(lat, lng); // 위도, 경도 범위 계산 코드(10m)

        System.out.println("minlat : "+lngLatRange.getMinLat() +"maxlat : "+lngLatRange.getMaxLat());
        System.out.println("minlng : "+lngLatRange.getMinLng() +"maxlng : "+lngLatRange.getMaxLng());

        return placeRepository.findByLatBetweenAndLngBetween(lngLatRange.getMinLat(), lngLatRange.getMaxLat(),
                lngLatRange.getMinLng(), lngLatRange.getMaxLng());//jpa
    }


    public LngLatRange rangeCalculation(Double lat, Double lng){

        final double EARTH_RADIUS = 6371.01 * 1000; // 지구의 반지름(미터 단위)

        double radius = 20; // 반경 (미터 단위)

        double latRange = toDegrees(radius / EARTH_RADIUS); // 위도 범위 계산
        double lngRange = toDegrees(radius / (EARTH_RADIUS * cos(toRadians(lat)))); // 경도 범위 계산

        LngLatRange lngLatRange = new LngLatRange();

        lngLatRange.setMinLat(lat - latRange); // 최소 위도 계산
        lngLatRange.setMaxLat(lat + latRange); // 최대 위도 계산
        lngLatRange.setMinLng(lng - lngRange); // 최소 경도 계산
        lngLatRange.setMaxLng(lng + lngRange); // 최대 경도 계산

        return lngLatRange;
    }

    public String endcodingBase64(MultipartFile image) throws IOException {

        byte[] bytes = image.getBytes();
        String base64String = Base64.encodeBase64String(bytes);
        return base64String;
    }

    public boolean containsOnlyEng(String input){
        return input.matches("[a-zA-Z]+");
    } //영어만 검출

    public String NoSpacing(String input){ //공백제거
        input = input.replaceAll("\\s", "");
        return input;
       // return input.replace(" ", "");
    }



    public double jacad(String dbtext, String ocrresult){ //ocrresult이 AI모델을 통해 받아온 값이라고 가정

        Set<String> set1 = new HashSet<>(15);
        Set<String> set2 = new HashSet<>(15);

        if (containsOnlyEng(NoSpacing(ocrresult))){ //영어로만 이루어져있을때
            dbtext = dbtext.toLowerCase();
            ocrresult = ocrresult.toLowerCase();

            char[] chars1 = dbtext.toCharArray();
            char[] chars2 = ocrresult.toCharArray();

            for(char ch:chars1){
                set1.add(String.valueOf(ch));
            }
            for(char ch:chars2){
                set2.add(String.valueOf(ch));
            }

        } else{
            CharSequence normalized1 = normalize(dbtext);
            CharSequence normalized2 = normalize(ocrresult);
            Seq<KoreanTokenizer.KoreanToken> tokens1 = tokenize(normalized1);
            Seq<KoreanTokenizer.KoreanToken> tokens2 = tokenize(normalized2);


            set1.addAll(tokensToJavaStringList(tokens1));
            set2.addAll(tokensToJavaStringList(tokens2));
        }

        System.out.println("jacad set 1 : " + set1);
        System.out.println("jacad set 2 : " + set2);


        // 두 개의 Set 객체의 교집합 크기를 구합니다.
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        int intersectionSize = intersection.size();

        // 두 개의 Set 객체의 합집합 크기를 구합니다.
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        int unionSize = union.size();

        System.out.println((double) intersectionSize / unionSize);

        // 자카드 유사도를 계산하여 반환합니다.
        return (double) intersectionSize / unionSize;
    }

   


}
