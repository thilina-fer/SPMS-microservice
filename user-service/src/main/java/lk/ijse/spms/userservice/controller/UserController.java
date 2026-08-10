package lk.ijse.spms.userservice.controller;

import jakarta.validation.Valid;
import lk.ijse.spms.userservice.dto.ResponseDTO;
import lk.ijse.spms.userservice.dto.UserLoginDTO;
import lk.ijse.spms.userservice.dto.UserRegisterDTO;
import lk.ijse.spms.userservice.dto.UserResponseDTO;
import lk.ijse.spms.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@Valid @RequestBody UserRegisterDTO dto) {
        UserResponseDTO response = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), "User registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@Valid @RequestBody UserLoginDTO dto) {
        UserResponseDTO response = userService.login(dto);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Login successful", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO response = userService.getUserById(id);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "User retrieved successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRegisterDTO dto) {
        UserResponseDTO response = userService.updateUser(id, dto);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "User updated successfully", response));
    }
}
