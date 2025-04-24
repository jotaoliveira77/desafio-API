package com.projetoapi.api_project.controller;

import com.projetoapi.api_project.model.moviesPackage.Movies;
import com.projetoapi.api_project.service.MoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movies")
public class MoviesController {

    @Autowired
    MoviesService moviesService;

    @GetMapping("/list-all")
    public List<Movies> listMovies(@RequestParam("pagina") int pagina,
                                   @RequestParam("tamanho") int tamanho) {
        return moviesService.listMovies(pagina, tamanho);
    }

    @GetMapping("/list-by-title")
    public List<Movies> listMovieByTitle(@RequestParam("title") String title,
                                         @RequestParam("pagina") int pagina,
                                         @RequestParam("tamanho") int tamanho  ) {
        return moviesService.findByTitle(title, pagina, tamanho);
    }

    @GetMapping("/list-by-director")
    public List<Movies> listMovieByDirector(@RequestParam("director") String director,
                                            @RequestParam("pagina") int pagina,
                                            @RequestParam("tamanho") int tamanho) {
        return moviesService.findByDirector(director, pagina, tamanho);
    }

    @GetMapping("/list-by-genre")
    public List<Movies> listMovieByGenre(@RequestParam("genre") String genre,
                                         @RequestParam("pagina") int pagina,
                                         @RequestParam("tamanho") int tamanho) {
        return moviesService.findByGenre(genre, pagina, tamanho);
    }

   @GetMapping("/search")
    public List<Movies> searchAllFields(@RequestParam("searchTerm") String searchTerm,
                                        @RequestParam("pagina") int pagina,
                                        @RequestParam("tamanho") int tamanho){
        return moviesService.searchAllFields(searchTerm, pagina, tamanho);
    }

}

