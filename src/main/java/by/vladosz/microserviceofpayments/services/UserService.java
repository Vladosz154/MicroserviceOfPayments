package by.vladosz.microserviceofpayments.services;

import by.vladosz.microserviceofpayments.dto.UserDTO;
import by.vladosz.microserviceofpayments.mappers.UserMapper;
import by.vladosz.microserviceofpayments.models.User;
import by.vladosz.microserviceofpayments.repositories.UserRepository;
import by.vladosz.microserviceofpayments.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;

    public Page<UserDTO> findAll(int pageNumber, int pageSize) {
        return userRepository.findAll(PageRequest.of(pageNumber, pageSize,
                        Sort.by(Sort.Direction.ASC, "userName")))
                .map(userMapper::toUserDTO);
    }

    public ResponseEntity<UserDTO> createUser(User user) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        user.setRoles(List.of(userRoleRepository.findByName("ROLE_USER").get()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(userMapper.toUserDTO(savedUser));
    }

    public UserDTO findUserById(Long user_id) {
        return userRepository.findById(user_id).map(userMapper::toUserDTO).orElseThrow(() ->
                new RuntimeException(" User not found with id: " + user_id));
    }

    public ResponseEntity<?> updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, " User not found with id: " + id));
        userMapper.updateFromUserDTO(user, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(userMapper.toUserDTO(updatedUser));
    }

    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.deleteById(id);
    }

    public Optional<User> findByUserName(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("User name cannot be null");
        }
        return userRepository.findByUserName(userName);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUserName(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }
}
