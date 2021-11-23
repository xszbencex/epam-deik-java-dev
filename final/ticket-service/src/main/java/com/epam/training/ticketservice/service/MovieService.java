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

    public void createMovie(String name, String genre, Integer length) {
        this.movieRepository.save(new Movie(name, genre, length));
    }

    public void updateMovie(String name, String genre, Integer length) throws NoSuchItemException {
        this.movieRepository.findById(name)
                .map(movie -> this.movieRepository.save(new Movie(name, genre, length)))
                .orElseThrow(() -> new NoSuchItemException("There is no movie with name: " + name));
    }

    public void deleteMovie(String name) throws NoSuchItemException {
        this.movieRepository.findById(name)
                .map(movie -> {
                    this.movieRepository.deleteById(name);
                    return movie;
                })
                .orElseThrow(() -> new NoSuchItemException("There is no movie with name: " + name));
    }
}
