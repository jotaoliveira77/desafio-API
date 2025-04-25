package com.projetoapi.api_project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.projetoapi.api_project.model.moviesPackage.Movies;
import com.projetoapi.api_project.repository.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
        return moviesRepository.findByTitle(title, PageRequest.of(pagina, tamanho)).getContent();
    }

    public List<Movies> findByDirector(String director, int pagina, int tamanho) {
        return moviesRepository.findByDirector(director, PageRequest.of(pagina, tamanho)).getContent();
    }

    public List<Movies> findByGenre(String genre, int pagina, int tamanho) {
        return moviesRepository.findByGenre(genre, PageRequest.of(pagina, tamanho)).getContent();
    }

    @Value("${tmdb.api.key}")
    private String tmdbApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public void importarFilmesPopularesDoTMDB () {
        for (int page = 1; page <= 5; page++) {
            String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + tmdbApiKey +
                    "&language=pt-BR&page=" + page;

            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            JsonNode results = response.get("results");

            for (JsonNode movieNode : results) {
                Long tmdbId = movieNode.get("id").asLong();
                String title = movieNode.get("title").asText();  // Agora definindo o título corretamente

                // Obter detalhes do filme
                String detailsUrl = "https://api.themoviedb.org/3/movie/" + tmdbId +
                        "?api_key=" + tmdbApiKey + "&language=pt-BR";
                JsonNode details = restTemplate.getForObject(detailsUrl, JsonNode.class);

                String genre = "Desconhecido";
                if (details.has("genres") && details.get("genres").isArray()) {
                    List<String> genres = new ArrayList<>();
                    for (JsonNode g : details.get("genres")) {
                        genres.add(g.get("name").asText());
                    }
                    genre = String.join(", ", genres);  // Concatenando os gêneros, se houver mais de um
                }

                // Obter créditos do filme (diretor)
                String creditsUrl = "https://api.themoviedb.org/3/movie/" + tmdbId + "/credits" +
                        "?api_key=" + tmdbApiKey + "&language=pt-BR";
                JsonNode credits = restTemplate.getForObject(creditsUrl, JsonNode.class);

                String director = "Desconhecido";
                if (credits.has("crew") && credits.get("crew").isArray()) {
                    for (JsonNode crewMember : credits.get("crew")) {
                        if (crewMember.get("job").asText().equals("Director")) {
                            director = crewMember.get("name").asText();  // Pega o nome do diretor
                            break;
                        }
                    }
                }

                // Salvar filme no banco de dados
                Movies movie = new Movies();
                movie.setTitle(title);
                movie.setGenre(genre);
                movie.setDirector(director);

                moviesRepository.save(movie);
            }
            }
        }
    }

