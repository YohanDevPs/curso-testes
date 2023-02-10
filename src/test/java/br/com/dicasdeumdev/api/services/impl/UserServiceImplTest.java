package br.com.dicasdeumdev.api.services.impl;

import br.com.dicasdeumdev.api.domain.User;
import br.com.dicasdeumdev.api.domain.dto.UserDTO;
import br.com.dicasdeumdev.api.exeptions.ObjectNotFoundException;
import br.com.dicasdeumdev.api.repositories.UserRepository;
import br.com.dicasdeumdev.api.services.exeptions.DataIntegratyViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    public static final Integer ID = 1;
    public static final String NAME = "Yohan";
    public static final String EMAIL = "yohan@gmail.com";
    public static final String PASSWORD = "1234";
    public static final String MSG_OBJECT_NOT_FOUND = "Objeto não encontrado";
    public static final int INDEX = 0;
    public static final String E_MAIL_JA_CADASTRADO_NO_SISTEMA = "E-mail já cadastrado no sistema";

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository repository;

    @Mock
    private ModelMapper modelMapper;

    private User user;
    private UserDTO userDTO;
    private Optional<User> optionalUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUser();
    }

    @Test
    void whenFindByIdTheReturnUserInstance() {
        when(repository.findById(anyInt())).thenReturn(optionalUser);

        User response = userService.findById(ID);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PASSWORD, response.getPassword());
    }

    @Test
    void whenFindByIdTheReturnObjectNotFoundException() {
        when(repository.findById(anyInt())).thenThrow(new ObjectNotFoundException(MSG_OBJECT_NOT_FOUND));

        try {
            userService.findById(ID);
        } catch (Exception ex) {
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals(MSG_OBJECT_NOT_FOUND, ex.getMessage());
        }
    }


    @Test
    void whenFindAllTheReturnAnListOfUsers() {
        when(repository.findAll()).thenReturn(List.of(user));

        var listResponse = userService.findAll();

        assertEquals(1, listResponse.size());
        assertEquals(User.class, listResponse.get(INDEX).getClass());

        assertEquals(ID, listResponse.get(INDEX).getId());
        assertEquals(NAME, listResponse.get(INDEX).getName());
        assertEquals(EMAIL, listResponse.get(INDEX).getEmail());
        assertEquals(PASSWORD, listResponse.get(INDEX).getPassword());
    }

    @Test
    void mustCreateNewUserAndReturnAUser() {
        when(repository.save(any())).thenReturn(user);

        var response = userService.create(userDTO);

        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PASSWORD, response.getPassword());
    }

    @Test
    void mustCreateNewUserAndReturnDataIntegratyViolationException() {
        when(repository.findByEmail(anyString())).thenReturn(optionalUser);

        try {
            optionalUser.get().setId(2);
            userService.create(userDTO);
        } catch (Exception ex) {
            assertEquals(DataIntegratyViolationException.class, ex.getClass());
            assertEquals(E_MAIL_JA_CADASTRADO_NO_SISTEMA, ex.getMessage());
        }
    }

    @Test
    void mustUpdateNewUserAndReturnAUser() {
        when(repository.save(any())).thenReturn(user);

        var response = userService.update(userDTO);

        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PASSWORD, response.getPassword());
    }

    @Test
    void mustUpdateAUserAndReturnDataIntegratyViolationException() {
        when(repository.findByEmail(anyString())).thenReturn(optionalUser);

        try {
            optionalUser.get().setId(2);
            userService.create(userDTO);
        } catch (Exception ex) {
            assertEquals(DataIntegratyViolationException.class, ex.getClass());
            assertEquals(E_MAIL_JA_CADASTRADO_NO_SISTEMA, ex.getMessage());
        }
    }

    @Test
    void deleteUserWithSuccess() {
        when(repository.findById(anyInt())).thenReturn(optionalUser);
        doNothing().when(repository).deleteById(anyInt());

        userService.delete(ID);
        verify(repository, times(1)).deleteById(anyInt());
    }

    @Test
    void deleteUserAndThrowsObjectNotFoundException() {
        when(repository.findById(anyInt())).thenThrow(new ObjectNotFoundException(MSG_OBJECT_NOT_FOUND));

        try {
            userService.delete(ID);
        } catch (Exception ex) {
            assertEquals(MSG_OBJECT_NOT_FOUND, ex.getMessage());
            assertEquals(ObjectNotFoundException.class, ex.getClass());
        }
    }

    private void startUser() {
        user = new User(ID, NAME, EMAIL, PASSWORD);
        userDTO = new UserDTO(ID, NAME, EMAIL, PASSWORD);
        optionalUser = Optional.of(new User(ID, NAME, EMAIL, PASSWORD));
    }
}