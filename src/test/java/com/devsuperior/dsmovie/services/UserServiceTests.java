package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.projections.UserDetailsProjection;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.tests.UserDetailsFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import com.devsuperior.dsmovie.utils.CustomUserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private CustomUserUtil userUtil;

    private String existingUsername, nonExistingUsername;
    private UserEntity user;

    @BeforeEach
    void setUp() throws Exception {

        existingUsername = UserFactory.USER_NAME;
        nonExistingUsername = "nonexistuser@gmail.com";

        user = UserFactory.createUserEntity();
        Mockito.when(repository.findByUsername(existingUsername)).thenReturn(Optional.of(user));
        Mockito.when(repository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());
    }

    @Test
    public void authenticatedShouldReturnUserEntityWhenUserExists() {
        Mockito.when(userUtil.getLoggedUsername()).thenReturn(existingUsername);
        UserEntity result = service.authenticated();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2L, result.getId());
        Assertions.assertEquals(existingUsername, result.getUsername());
    }

    @Test
    public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
        Mockito.doThrow(ClassCastException.class).when(userUtil).getLoggedUsername();
        Assertions.assertThrows(UsernameNotFoundException.class, ()->{
            service.authenticated();
        });
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
    }

    @Test
    public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
    }
}
