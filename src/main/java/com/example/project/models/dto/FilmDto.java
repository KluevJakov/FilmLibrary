package com.example.project.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilmDto {
    private Long id;
    private String title;
    private String genre;
    private Long ageRestriction;
    private Long year;
    private String country;
    private String director;
    private String scenarist;
    private String producer;
    private Long budget;
    private Long fee;
    private Long time;
    private String image;
    private Integer like; //0 - не указано, 1 - нравится, 2 - не нравится
    private Integer view; //0 - не указано, 1 - просмотрено, 2 - не досмотрено
}
