package lk.ijse.spms.userservice.service;

import lk.ijse.spms.userservice.dto.UserLoginDTO;
import lk.ijse.spms.userservice.dto.UserRegisterDTO;
import lk.ijse.spms.userservice.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO register(UserRegisterDTO dto);
    UserResponseDTO login(UserLoginDTO dto);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO updateUser(Long id, UserRegisterDTO dto);
}
