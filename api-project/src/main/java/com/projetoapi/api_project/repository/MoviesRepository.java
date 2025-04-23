package com.projetoapi.api_project.repository;

import com.projetoapi.api_project.model.moviesPackage.Movies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoviesRepository extends PagingAndSortingRepository<Movies, Long> {

    Page<Movies> findByTitle(String title, Pageable pageable);
    Page<Movies> findByDirector(String director, Pageable pageable);
    Page<Movies> findByGenre(String genre, Pageable pageable);

}
