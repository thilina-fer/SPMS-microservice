package lk.ijse.spms.vehicleservice.service.impl;

import lk.ijse.spms.vehicleservice.dto.VehicleRegisterDTO;
import lk.ijse.spms.vehicleservice.dto.VehicleResponseDTO;
import lk.ijse.spms.vehicleservice.dto.VehicleUpdateDTO;
import lk.ijse.spms.vehicleservice.entity.Vehicle;
import lk.ijse.spms.vehicleservice.entity.VehicleStatus;
import lk.ijse.spms.vehicleservice.exception.PlateNumberAlreadyExistsException;
import lk.ijse.spms.vehicleservice.exception.VehicleNotFoundException;
import lk.ijse.spms.vehicleservice.repository.VehicleRepository;
import lk.ijse.spms.vehicleservice.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    public VehicleResponseDTO createVehicle(VehicleRegisterDTO dto) {
        if (vehicleRepository.existsByPlateNumber(dto.getPlateNumber())) {
            throw new PlateNumberAlreadyExistsException("Plate number already registered: " + dto.getPlateNumber());
        }

        Vehicle vehicle = Vehicle.builder()
                .plateNumber(dto.getPlateNumber())
                .type(dto.getType())
                .userId(dto.getUserId())
                .status(VehicleStatus.OUT)
                .build();

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return mapToResponseDTO(savedVehicle);
    }

    @Override
    public VehicleResponseDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + id));
        return mapToResponseDTO(vehicle);
    }

    @Override
    public VehicleResponseDTO updateVehicle(Long id, VehicleUpdateDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + id));

        vehicleRepository.findByPlateNumber(dto.getPlateNumber()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new PlateNumberAlreadyExistsException("Plate number already registered: " + dto.getPlateNumber());
            }
        });

        vehicle.setPlateNumber(dto.getPlateNumber());
        vehicle.setType(dto.getType());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return mapToResponseDTO(updatedVehicle);
    }

    @Override
    public List<VehicleResponseDTO> getVehiclesByUserId(Long userId) {
        return vehicleRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public VehicleResponseDTO markEntry(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + id));

        vehicle.setStatus(VehicleStatus.IN);
        vehicle.setEntryTime(LocalDateTime.now());
        vehicle.setExitTime(null);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return mapToResponseDTO(updatedVehicle);
    }

    @Override
    public VehicleResponseDTO markExit(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + id));

        vehicle.setStatus(VehicleStatus.OUT);
        vehicle.setExitTime(LocalDateTime.now());
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return mapToResponseDTO(updatedVehicle);
    }

    private VehicleResponseDTO mapToResponseDTO(Vehicle vehicle) {
        return VehicleResponseDTO.builder()
                .id(vehicle.getId())
                .plateNumber(vehicle.getPlateNumber())
                .type(vehicle.getType())
                .userId(vehicle.getUserId())
                .status(vehicle.getStatus())
                .entryTime(vehicle.getEntryTime())
                .exitTime(vehicle.getExitTime())
                .build();
    }
}
