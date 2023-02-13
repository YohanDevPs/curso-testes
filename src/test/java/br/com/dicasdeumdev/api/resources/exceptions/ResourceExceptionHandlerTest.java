package br.com.dicasdeumdev.api.resources.exceptions;

import br.com.dicasdeumdev.api.exeptions.ObjectNotFoundException;
import br.com.dicasdeumdev.api.services.exeptions.DataIntegratyViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ResourceExceptionHandlerTest {

    public static final String MSG_INVALID_EMAIL = "E-mail já cadastrado no sistema";
    public static final String OBJECT_NOT_FOUND = "Objeto não encontrado";
    @InjectMocks
    private ResourceExceptionHandler exceptionHandler;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenObjectNotFoundExceptionThenReturnAResponseEntity() {
        ResponseEntity<StandartError> response = exceptionHandler
                .objectNotFound(new ObjectNotFoundException(OBJECT_NOT_FOUND), new MockHttpServletRequest());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(OBJECT_NOT_FOUND, response.getBody().getError());
    }

    @Test
    void whenDataIntegratyViolationThenReturnAResponseEntity() {
        ResponseEntity<StandartError> response = exceptionHandler
                .dataIntegratyViolation(
                        new DataIntegratyViolationException(MSG_INVALID_EMAIL),
                        new MockHttpServletRequest()
                );

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals(MSG_INVALID_EMAIL, response.getBody().getError());
    }
}