package com.example.PlaceAR.dto;

import com.google.maps.model.LatLng;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import org.locationtech.jts.geom.Point;


@Getter
@Setter
@Entity
public class PlaceDTO {

    @Id
    private String Place_id;
//    private Point location;
    private Double lng;
    private Double lat;
    private String name;
    private String address;
    private String number;
    private String open_hours;
    private String site;
    private Float rating;

}


