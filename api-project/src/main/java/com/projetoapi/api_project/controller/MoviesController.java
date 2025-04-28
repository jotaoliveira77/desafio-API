package com.projetoapi.api_project.controller;

import com.projetoapi.api_project.model.moviesPackage.Movies;
import com.projetoapi.api_project.model.usersPackage.Users;
import com.projetoapi.api_project.service.MoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

       Users user = new Users();
       if( user.isEnabled() == false) {
           return null;
       }

       return moviesService.searchAllFields(searchTerm, pagina, tamanho);
    }

    @PostMapping("/importar-tmdb")
    public ResponseEntity<String> importarFilmes() {
        moviesService.importarFilmesPopularesDoTMDB();
        return ResponseEntity.ok("Importação feita!");
    }
}

