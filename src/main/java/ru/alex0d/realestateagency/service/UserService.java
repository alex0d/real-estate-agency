package ru.alex0d.realestateagency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.alex0d.realestateagency.dto.SignUpDto;
import ru.alex0d.realestateagency.model.RealEstate;
import ru.alex0d.realestateagency.model.User;
import ru.alex0d.realestateagency.repository.RoleRepository;
import ru.alex0d.realestateagency.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RealEstateService realEstateService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User registerUser(SignUpDto signUpDto) {
        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setFirstName(signUpDto.getFirstName());
        user.setLastName(signUpDto.getLastName());
        user.setEmail(signUpDto.getEmail());
        user.setPhoneNumber(signUpDto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByName("ROLE_USER")));
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(Long id) {
        Set<RealEstate> realEstates = userRepository.findById(id).orElseThrow().getRealEstates();
        for (RealEstate realEstate : realEstates) {
            realEstateService.deleteById(realEstate.getId());
        }
        userRepository.deleteById(id);
    }
    
}
