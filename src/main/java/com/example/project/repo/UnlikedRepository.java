package com.example.project.repo;

import com.example.project.models.Unliked;
import com.example.project.models.Viewed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UnlikedRepository extends JpaRepository<Unliked, Long> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE from unliked WHERE user_id = ?1 and film_id = ?2")
    void removeByUserIdAndFilmId(Long userId, Long filmId);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) from unliked WHERE user_id = ?1 and film_id = ?2")
    int selectByUserIdAndFilmId(Long userId, Long filmId);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) from unliked WHERE user_id = ?1")
    int countByUserId(Long userId);

    @Query(nativeQuery = true, value = "SELECT * from unliked WHERE user_id != ?1")
    List<Unliked> findAllWithoutMe(Long userId);

    @Query(nativeQuery = true, value = "SELECT * from unliked WHERE user_id = ?1")
    List<Unliked> findMy(Long userId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE from unliked WHERE user_id = ?1")
    void removeByUserId(Long userId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE from unliked WHERE film_id = ?1")
    void removeByFilmId(Long filmId);
}
