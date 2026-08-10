package lk.ijse.spms.userservice.service.impl;

import lk.ijse.spms.userservice.dto.UserLoginDTO;
import lk.ijse.spms.userservice.dto.UserRegisterDTO;
import lk.ijse.spms.userservice.dto.UserResponseDTO;
import lk.ijse.spms.userservice.entity.Role;
import lk.ijse.spms.userservice.entity.User;
import lk.ijse.spms.userservice.exception.EmailAlreadyExistsException;
import lk.ijse.spms.userservice.exception.InvalidCredentialsException;
import lk.ijse.spms.userservice.exception.UserNotFoundException;
import lk.ijse.spms.userservice.repository.UserRepository;
import lk.ijse.spms.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO register(UserRegisterDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use: " + dto.getEmail());
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole() != null ? dto.getRole() : Role.DRIVER)
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponseDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO login(UserLoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return mapToResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return mapToResponseDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRegisterDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (!user.getEmail().equalsIgnoreCase(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use: " + dto.getEmail());
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return mapToResponseDTO(updatedUser);
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
