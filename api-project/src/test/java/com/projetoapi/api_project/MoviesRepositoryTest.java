package com.projetoapi.api_project;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.projetoapi.api_project.model.moviesPackage.Movies;
import com.projetoapi.api_project.repository.MoviesRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // usa H2
@Transactional
class MoviesRepositoryTest {

    @Autowired
    private MoviesRepository moviesRepository;

    @BeforeEach
    void setUp() {
        moviesRepository.save(new Movies(null, "The Matrix", "Lana Wachowski", "Sci-Fi"));
        moviesRepository.save(new Movies(null, "Inception", "Christopher Nolan", "Sci-Fi"));
        moviesRepository.save(new Movies(null, "Interstellar", "Christopher Nolan", "Sci-Fi"));
        moviesRepository.save(new Movies(null, "Titanic", "James Cameron", "Romance"));
    }

    @Test
    void shouldFindMoviesByTitle() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Movies> result = moviesRepository.searchAllFields("matrix", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("The Matrix", result.getContent().get(0).getTitle());
    }

    @Test
    void shouldFindMoviesByDirector() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Movies> result = moviesRepository.searchAllFields("nolan", pageable);

        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent()
                .stream()
                .allMatch(movie -> movie.getDirector().toLowerCase().contains("nolan")));
    }

    @Test
    void shouldFindMoviesByGenre() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Movies> result = moviesRepository.searchAllFields("romance", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Titanic", result.getContent().get(0).getTitle());
    }

    @Test
    void shouldReturnEmptyWhenNoMatch() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Movies> result = moviesRepository.searchAllFields("horror", pageable);

        assertEquals(0, result.getTotalElements());
    }
}