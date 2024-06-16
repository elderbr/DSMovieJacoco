package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.Optional;

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

		// findAll
		Mockito.when(repository.searchByTitle(any(), (Pageable) any())).thenReturn(page);

		// FindById
		Mockito.when(repository.findById(existingID)).thenReturn(Optional.of(movie));
		Mockito.when(repository.findById(nonExistingID)).thenThrow(ResourceNotFoundException.class);

		// Save
		Mockito.when(repository.save(any())).thenReturn(movie);

		// Update
		Mockito.when(repository.getReferenceById(existingID)).thenReturn(movie);
		Mockito.when(repository.save(any())).thenReturn(movie);
		Mockito.when(repository.getReferenceById(nonExistingID)).thenThrow(EntityNotFoundException.class);

		// Delete
		Mockito.when(repository.existsById(existingID)).thenReturn(true);

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
		MovieDTO result = service.findById(existingID);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(1L, result.getId());
		Assertions.assertEquals(title, result.getTitle());
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.findById(nonExistingID);
		});
	}
	
	@Test
	public void insertShouldReturnMovieDTO() {
		MovieDTO result = service.insert(movieDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingID, result.getId());
		Assertions.assertEquals(title, result.getTitle());
	}
	
	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {
		MovieDTO result = service.update(existingID, movieDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingID, result.getId());
		Assertions.assertEquals(title, result.getTitle());
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.update(nonExistingID, movieDTO);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingID);
		});
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
	}
}
