package com.example.PlaceAR.repository;

import com.example.PlaceAR.dto.PlaceDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;


@EnableJpaRepositories
public interface PlaceRepository extends JpaRepository<PlaceDTO, Integer> {
    PlaceDTO findByName(String name);

    List<PlaceDTO> findByLatBetweenAndLngBetween(Double minlat, Double maxlat, Double minlng, Double maxlng);
}
