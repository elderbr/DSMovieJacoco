package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import com.devsuperior.dsmovie.utils.CustomScoreSum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {

    @InjectMocks
    private ScoreService scoreService;

    @Mock
    private ScoreRepository scoreRepository;

    @Mock
    private CustomScoreSum scoreSum;

    @Mock
    private UserService userService;

    @Mock
    private MovieRepository movieRepository;
    private MovieEntity movie;
    private Long existingMovieID, nonExistingMoviID;

    private UserEntity user;
    private ScoreEntity score;
    private ScoreDTO scoreDTO;

    @BeforeEach
    void setUp() throws Exception {

        user = UserFactory.createUserEntity();
        score = ScoreFactory.createScoreEntity();
        scoreDTO = ScoreFactory.createScoreDTO();

        movie = MovieFactory.createMovieEntity();
        existingMovieID = 1L;
        nonExistingMoviID = 2L;

        // Movie
        Mockito.when(movieRepository.findById(existingMovieID)).thenReturn(Optional.of(movie));
        Mockito.when(movieRepository.findById(nonExistingMoviID)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(movieRepository.save(any())).thenReturn(movie);

        // Score
        Mockito.when(scoreRepository.saveAndFlush(any())).thenReturn(score);
    }

    @Test
    public void saveScoreShouldReturnMovieDTO() {
        Mockito.when(userService.authenticated()).thenReturn(user);
        Mockito.when(scoreSum.getScoreSum(any())).thenReturn(score.getId().getMovie().getScore());

        MovieDTO result = scoreService.saveScore(scoreDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        //Assertions.assertEquals(ScoreFactory.scoreValue, result.getScore());
    }

    @Test
    public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
        Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            Mockito.when(movieRepository.findById(nonExistingMoviID));
        });
    }
}
