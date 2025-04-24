package com.projetoapi.api_project.service;

import com.projetoapi.api_project.model.moviesPackage.Movies;
import com.projetoapi.api_project.repository.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class MoviesService {

    @Autowired
    MoviesRepository moviesRepository;


    public List<Movies> searchAllFields(String searchTerm, int pagina, int tamanho) {
        return moviesRepository.searchAllFields(searchTerm, PageRequest.of(pagina, tamanho)).getContent();
    }

    public List<Movies> listMovies(int pagina, int tamanho) {
        return moviesRepository.findAll(PageRequest.of(pagina, tamanho)).getContent();
    }

    public List<Movies> findByTitle(String title, int pagina, int tamanho) {
        return moviesRepository.findByTitle(title, PageRequest.of( pagina, tamanho)).getContent();
    }

    public List<Movies> findByDirector(String director, int pagina, int tamanho) {
        return moviesRepository.findByDirector(director, PageRequest.of( pagina, tamanho)).getContent();
    }

    public List<Movies> findByGenre(String genre,  int pagina, int tamanho) {
        return moviesRepository.findByGenre(genre, PageRequest.of( pagina, tamanho)).getContent();


    }
}
