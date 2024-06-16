package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.tests.MovieFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService service;

	@Mock
	private MovieRepository repository;

	private String title;
	private MovieEntity movie;
	private MovieDTO movieDTO;
	private Long nonExistingID, existingID;

	private PageImpl<MovieEntity> page;
	private Pageable pageable;

	@BeforeEach
	void setUp() throws Exception{

		title = "Test Movie";
		existingID = 1L;
		nonExistingID = 2L;

		movie = MovieFactory.createMovieEntity();
		movieDTO = MovieFactory.createMovieDTO();

		page = new PageImpl<>(List.of(movie));
		pageable = PageRequest.of(0, 12);

		Mockito.when(repository.searchByTitle(any(), (Pageable) any())).thenReturn(page);
	}

	
	@Test
	public void findAllShouldReturnPagedMovieDTO() {

		Page<MovieDTO> result = service.findAll(title, pageable);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getSize());
		Assertions.assertEquals(title, result.iterator().next().getTitle());
	}
	
	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
	}
	
	@Test
	public void insertShouldReturnMovieDTO() {
	}
	
	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
	}
}
