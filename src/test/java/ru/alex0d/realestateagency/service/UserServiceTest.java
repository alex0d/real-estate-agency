package ru.alex0d.realestateagency.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.alex0d.realestateagency.dto.SignUpDto;
import ru.alex0d.realestateagency.model.RealEstate;
import ru.alex0d.realestateagency.model.Role;
import ru.alex0d.realestateagency.model.User;
import ru.alex0d.realestateagency.repository.RoleRepository;
import ru.alex0d.realestateagency.repository.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RealEstateService realEstateService;

    @InjectMocks
    private UserService userService;

    @Test
    public void testLoadUserByUsername() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("password");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("test");

        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    public void testFindByUsername() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        User foundUser = userService.findByUsername("test");

        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setEmail("test@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User foundUser = userService.findByEmail("test@example.com");

        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void testRegisterUser() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername("test");
        signUpDto.setFirstName("John");
        signUpDto.setLastName("Doe");
        signUpDto.setEmail("test@example.com");
        signUpDto.setPhoneNumber("+123456789");
        signUpDto.setPassword("password");

        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        when(roleRepository.findByName(anyString())).thenReturn(role);

        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@example.com");
        user.setPhoneNumber("+123456789");
        user.setPassword("encodedPassword");
        user.setRoles(Collections.singleton(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.registerUser(signUpDto);

        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getPhoneNumber(), savedUser.getPhoneNumber());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertEquals(user.getRoles(), savedUser.getRoles());
        verify(roleRepository, times(1)).findByName(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testFindAll() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("test1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("test2");

        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userService.findAll();

        assertEquals(2, foundUsers.size());
        assertEquals(user1, foundUsers.get(0));
        assertEquals(user2, foundUsers.get(1));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteById() {
        User user = new User();
        user.setId(1L);
        Set<RealEstate> realEstates = new HashSet<>();
        RealEstate realEstate1 = new RealEstate();
        realEstate1.setId(1L);
        realEstate1.setOwner(user);
        RealEstate realEstate2 = new RealEstate();
        realEstate2.setId(2L);
        realEstate2.setOwner(user);
        realEstates.add(realEstate1);
        realEstates.add(realEstate2);
        user.setRealEstates(realEstates);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        doNothing().when(realEstateService).deleteById(anyLong());

        userService.deleteById(1L);

        verify(realEstateService, times(1)).deleteById(1L);
        verify(realEstateService, times(1)).deleteById(2L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}