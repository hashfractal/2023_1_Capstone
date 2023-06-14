package com.example.PlaceAR.dto;

import com.google.maps.model.LatLng;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.locationtech.jts.geom.Point;


@Getter
@Setter
@Entity
@Table(name="placedto")
public class PlaceDTO {

    @Id
    private String Place_id;
//    private Point location;

    @Column(name = "lng")
    private Double lng;
    @Column(name = "lat")
    private Double lat;

    private String name;
    private String address;
    private String number;
    private String open_hours;
    private String site;
    private Float rating;

}


