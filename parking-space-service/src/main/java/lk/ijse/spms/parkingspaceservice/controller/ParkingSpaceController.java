package lk.ijse.spms.parkingspaceservice.controller;

import jakarta.validation.Valid;
import lk.ijse.spms.parkingspaceservice.dto.ParkingSpaceCreateDTO;
import lk.ijse.spms.parkingspaceservice.dto.ParkingSpaceResponseDTO;
import lk.ijse.spms.parkingspaceservice.dto.ParkingSpaceUpdateDTO;
import lk.ijse.spms.parkingspaceservice.dto.ResponseDTO;
import lk.ijse.spms.parkingspaceservice.dto.StatusUpdateDTO;
import lk.ijse.spms.parkingspaceservice.entity.ParkingSpaceStatus;
import lk.ijse.spms.parkingspaceservice.service.ParkingSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parking")
@RequiredArgsConstructor
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;

    @PostMapping
    public ResponseEntity<ResponseDTO> createSpace(@Valid @RequestBody ParkingSpaceCreateDTO dto) {
        ParkingSpaceResponseDTO response = parkingSpaceService.createSpace(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), "Parking space created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> searchSpaces(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String zone,
            @RequestParam(required = false) Boolean available) {
        List<ParkingSpaceResponseDTO> response = parkingSpaceService.searchSpaces(city, zone, available);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Parking spaces retrieved successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getSpaceById(@PathVariable Long id) {
        ParkingSpaceResponseDTO response = parkingSpaceService.getSpaceById(id);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Parking space retrieved successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateSpace(
            @PathVariable Long id,
            @Valid @RequestBody ParkingSpaceUpdateDTO dto) {
        ParkingSpaceResponseDTO response = parkingSpaceService.updateSpace(id, dto);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Parking space updated successfully", response));
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<ResponseDTO> reserveSpace(@PathVariable Long id) {
        ParkingSpaceResponseDTO response = parkingSpaceService.reserveSpace(id);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Parking space reserved successfully", response));
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<ResponseDTO> releaseSpace(@PathVariable Long id) {
        ParkingSpaceResponseDTO response = parkingSpaceService.releaseSpace(id);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Parking space released successfully", response));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody(required = false) StatusUpdateDTO dto,
            @RequestParam(required = false) ParkingSpaceStatus status) {
        ParkingSpaceStatus targetStatus = (dto != null && dto.getStatus() != null) ? dto.getStatus() : status;
        if (targetStatus == null) {
            throw new IllegalArgumentException("Status must be provided in the request body or query parameter");
        }
        ParkingSpaceResponseDTO response = parkingSpaceService.updateStatus(id, targetStatus);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Parking space status updated successfully", response));
    }
}
