package com.example.project.repo;

import com.example.project.models.Liked;
import com.example.project.models.Thrown;
import com.example.project.models.Unliked;
import com.example.project.models.Viewed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ThrownRepository extends JpaRepository<Thrown, Long> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE from thrown WHERE user_id = ?1 and film_id = ?2")
    void removeByUserIdAndFilmId(Long userId, Long filmId);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) from thrown WHERE user_id = ?1 and film_id = ?2")
    int selectByUserIdAndFilmId(Long userId, Long filmId);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) from thrown WHERE user_id = ?1")
    int countByUserId(Long userId);

    @Query(nativeQuery = true, value = "SELECT * from thrown WHERE user_id != ?1")
    List<Thrown> findAllWithoutMe(Long userId);

    @Query(nativeQuery = true, value = "SELECT * from thrown WHERE user_id = ?1")
    List<Thrown> findMy(Long userId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE from thrown WHERE user_id = ?1")
    void removeByUserId(Long userId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE from thrown WHERE film_id = ?1")
    void removeByFilmId(Long filmId);
}
