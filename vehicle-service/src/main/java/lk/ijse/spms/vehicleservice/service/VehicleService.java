package lk.ijse.spms.vehicleservice.service;

import lk.ijse.spms.vehicleservice.dto.VehicleRegisterDTO;
import lk.ijse.spms.vehicleservice.dto.VehicleResponseDTO;
import lk.ijse.spms.vehicleservice.dto.VehicleUpdateDTO;

import java.util.List;

public interface VehicleService {
    VehicleResponseDTO createVehicle(VehicleRegisterDTO dto);
    VehicleResponseDTO getVehicleById(Long id);
    VehicleResponseDTO updateVehicle(Long id, VehicleUpdateDTO dto);
    List<VehicleResponseDTO> getVehiclesByUserId(Long userId);
    VehicleResponseDTO markEntry(Long id);
    VehicleResponseDTO markExit(Long id);
}
