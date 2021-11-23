package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.repositry.MovieRepository;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Optional<Movie> getMovieById(String name) {
        return this.movieRepository.findById(name);
    }

    public List<Movie> getAllMovies() {
        return this.movieRepository.findAll();
    }

    public void createMovie(Movie movie) {
        this.movieRepository.save(movie);
    }

    public void updateMovie(Movie movie) throws NoSuchItemException {
        this.movieRepository.findById(movie.getName())
                .map(currentMovie -> this.movieRepository.save(movie))
                .orElseThrow(() -> new NoSuchItemException("There is no movie with name: " + movie.getName()));
    }

    public void deleteMovie(String name) throws NoSuchItemException {
        this.movieRepository.findById(name)
                .map(movie -> {
                    this.movieRepository.deleteById(name);
                    return movie;
                })
                .orElseThrow(() -> new NoSuchItemException("There is no movie with name: " + name));
    }

    public String formattedMovieList(List<Movie> movies) {
        StringBuilder stringBuilder = new StringBuilder();
        movies.forEach(movie -> stringBuilder.append(movie.toString()).append("\n"));
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
