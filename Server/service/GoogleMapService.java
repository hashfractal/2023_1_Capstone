package com.example.PlaceAR.service;

import com.example.PlaceAR.dto.PlaceDTO;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GoogleMapService {

    GeoApiContext context = new GeoApiContext.Builder()
            .apiKey("")
            .build();

    public List<PlaceDTO> neardySearch(double lat, double lng) throws IOException, InterruptedException, ApiException {

        LatLng location = new LatLng(lat, lng);
        PlacesSearchResponse response;
        String nextPageToken = null;
        List<PlaceDTO> DTOlist = new ArrayList<>();

        do{
            response = null;

            if(nextPageToken == null){
                response = PlacesApi.nearbySearchQuery(context,location)
                        .radius(100)
                        .language("ko")
                        .await();
            }else{

                response = PlacesApi.nearbySearchNextPage(context, nextPageToken).await();
            }

            for (PlacesSearchResult result : response.results){

                PlaceDetails placeDetails = new PlaceDetailsRequest(context)
                        .placeId(result.placeId)
                        .await();

                PlaceDTO placeDTO = setPlaceDTO(result.placeId, placeDetails.formattedAddress, placeDetails.geometry.location.lng,
                        placeDetails.geometry.location.lat, placeDetails.name, placeDetails.formattedPhoneNumber, getOpeningHoursText(placeDetails.openingHours),
                        String.valueOf(placeDetails.website), placeDetails.rating);

                DTOlist.add(placeDTO);
            }
            nextPageToken = response.nextPageToken;
        } while (nextPageToken != null);


        return DTOlist;
    }

    public PlaceDTO setPlaceDTO(String placeId, String Address, Double lng, Double lat, String name, String number,
                                String open_hours, String site, Float rating){

        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setPlace_id(placeId);
        placeDTO.setAddress(Address);

        placeDTO.setLng(lng);
        placeDTO.setLat(lat);
        placeDTO.setName(name);
        placeDTO.setNumber(number);
        placeDTO.setOpen_hours(open_hours);
        placeDTO.setSite(site);
        placeDTO.setRating(rating);

        return placeDTO;
    }

    public String getOpeningHoursText(OpeningHours openingHours){
        if(openingHours != null && openingHours.weekdayText != null){
            return Arrays.toString(openingHours.weekdayText);
        }else{
            return null;
        }
    }


}
