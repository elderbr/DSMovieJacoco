package com.devsuperior.dsmovie.utils;

import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomScoreSum {
    public Double getScoreSum(MovieEntity movie) {
        double sum = 0.0;
        for (ScoreEntity s : movie.getScores()) {
            sum = sum + s.getValue();
        }
        return sum / movie.getScores().size();
    }
}
