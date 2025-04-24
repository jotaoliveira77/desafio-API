package com.projetoapi.api_project.repository;

import com.projetoapi.api_project.model.moviesPackage.Movies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoviesRepository extends JpaRepository<Movies, Long> {

    @Query("SELECT m FROM Movies m WHERE " +
            "LOWER(m.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(m.director) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(m.genre) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Movies> searchAllFields(@org.springframework.data.repository.query.Param("searchTerm") String searchTerm, Pageable pageable);



    Page<Movies> findByTitle(String title, Pageable pageable);
    Page<Movies> findByDirector(String director, Pageable pageable);
    Page<Movies> findByGenre(String genre, Pageable pageable);

}
