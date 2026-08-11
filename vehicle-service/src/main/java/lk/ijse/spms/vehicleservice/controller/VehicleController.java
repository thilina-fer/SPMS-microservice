package lk.ijse.spms.vehicleservice.controller;

import jakarta.validation.Valid;
import lk.ijse.spms.vehicleservice.dto.ResponseDTO;
import lk.ijse.spms.vehicleservice.dto.VehicleRegisterDTO;
import lk.ijse.spms.vehicleservice.dto.VehicleResponseDTO;
import lk.ijse.spms.vehicleservice.dto.VehicleUpdateDTO;
import lk.ijse.spms.vehicleservice.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<ResponseDTO> createVehicle(@Valid @RequestBody VehicleRegisterDTO dto) {
        VehicleResponseDTO response = vehicleService.createVehicle(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), "Vehicle registered successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getVehicleById(@PathVariable Long id) {
        VehicleResponseDTO response = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Vehicle retrieved successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleUpdateDTO dto) {
        VehicleResponseDTO response = vehicleService.updateVehicle(id, dto);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Vehicle updated successfully", response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDTO> getVehiclesByUserId(@PathVariable Long userId) {
        List<VehicleResponseDTO> response = vehicleService.getVehiclesByUserId(userId);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "User vehicles retrieved successfully", response));
    }

    @PostMapping("/{id}/entry")
    public ResponseEntity<ResponseDTO> markEntry(@PathVariable Long id) {
        VehicleResponseDTO response = vehicleService.markEntry(id);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Vehicle entry recorded successfully", response));
    }

    @PostMapping("/{id}/exit")
    public ResponseEntity<ResponseDTO> markExit(@PathVariable Long id) {
        VehicleResponseDTO response = vehicleService.markExit(id);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Vehicle exit recorded successfully", response));
    }
}
