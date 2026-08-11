package lk.ijse.spms.parkingspaceservice.service;

import lk.ijse.spms.parkingspaceservice.dto.ParkingSpaceCreateDTO;
import lk.ijse.spms.parkingspaceservice.dto.ParkingSpaceResponseDTO;
import lk.ijse.spms.parkingspaceservice.dto.ParkingSpaceUpdateDTO;
import lk.ijse.spms.parkingspaceservice.entity.ParkingSpaceStatus;

import java.util.List;

public interface ParkingSpaceService {

    ParkingSpaceResponseDTO createSpace(ParkingSpaceCreateDTO dto);

    ParkingSpaceResponseDTO getSpaceById(Long id);

    ParkingSpaceResponseDTO updateSpace(Long id, ParkingSpaceUpdateDTO dto);

    List<ParkingSpaceResponseDTO> searchSpaces(String city, String zone, Boolean available);

    ParkingSpaceResponseDTO reserveSpace(Long id);

    ParkingSpaceResponseDTO releaseSpace(Long id);

    ParkingSpaceResponseDTO updateStatus(Long id, ParkingSpaceStatus status);
}
