package com.example.project.repo;

import com.example.project.models.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    @Query(value = "select distinct * from films where LOWER(title) like CONCAT('%', :search, '%') limit 20", nativeQuery = true)
    List<Film> findFilmsSearch(@Param("search") String search);

    @Query(value = "select distinct * from films limit 20", nativeQuery = true)
    List<Film> findFilms();

    @Query(value = "select distinct * from films limit 10", nativeQuery = true)
    List<Film> findStandartFilms();
}
