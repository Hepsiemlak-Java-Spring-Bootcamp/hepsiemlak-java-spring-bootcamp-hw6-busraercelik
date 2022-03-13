package emlakburada;

import emlakburada.dto.AuthRequest;
import emlakburada.dto.AuthResponse;
import emlakburada.entity.User;
import emlakburada.entity.enums.UserType;
import emlakburada.exception.UserNotFoundException;
import emlakburada.exception.UserPasswordNotValidException;
import emlakburada.repository.AuthRepository;
import emlakburada.service.AuthService;
import emlakburada.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthRepository authRepository;

    @Test
    void getToken_successful() {
        User user = prepareUser();
        Mockito.when(authRepository.findByEmail("abc@gmail.com"))
                .thenReturn(Optional.of(user));
        AuthResponse serviceToken = authService.getToken(prepareAuthRequest(user));
        assertNotNull(serviceToken);
    }

    @Test
    void getToken_UserNotFound() {
        assertThrows(UserNotFoundException.class, () -> authService.getToken(prepareAuthRequest(prepareUser())));
    }

    @Test
    void getToken_userPasswordNotValid() {
        User user = prepareUser();
        Mockito.when(authRepository.findByEmail("abc@gmail.com"))
                .thenReturn(Optional.of(user));
        AuthRequest authRequest = prepareAuthRequest(user);
        authRequest.setPassword("wrongPassword.");
        assertThrows(UserPasswordNotValidException.class, () -> authService.getToken(authRequest));
    }


    public User prepareUser() {
        User user = new User();
        user.setUserType(UserType.INDIVIDUAL);
        user.setEmail("abc@gmail.com");
        user.setId(5);
        user.setPassword("password");
        return user;
    }

    public AuthRequest prepareAuthRequest(User user) {
        AuthRequest request = new AuthRequest(user.getEmail(), user.getPassword());
        return request;
    }
}
