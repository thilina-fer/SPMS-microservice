package lk.ijse.spms.parkingspaceservice.service.impl;

import lk.ijse.spms.parkingspaceservice.dto.ParkingSpaceCreateDTO;
import lk.ijse.spms.parkingspaceservice.dto.ParkingSpaceResponseDTO;
import lk.ijse.spms.parkingspaceservice.dto.ParkingSpaceUpdateDTO;
import lk.ijse.spms.parkingspaceservice.entity.ParkingSpace;
import lk.ijse.spms.parkingspaceservice.entity.ParkingSpaceStatus;
import lk.ijse.spms.parkingspaceservice.exception.ParkingSpaceNotFoundException;
import lk.ijse.spms.parkingspaceservice.exception.SpaceNotAvailableException;
import lk.ijse.spms.parkingspaceservice.repository.ParkingSpaceRepository;
import lk.ijse.spms.parkingspaceservice.service.ParkingSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    private final ParkingSpaceRepository parkingSpaceRepository;

    @Override
    public ParkingSpaceResponseDTO createSpace(ParkingSpaceCreateDTO dto) {
        ParkingSpace parkingSpace = ParkingSpace.builder()
                .zone(dto.getZone())
                .city(dto.getCity())
                .ownerId(dto.getOwnerId())
                .pricePerHour(dto.getPricePerHour())
                .status(ParkingSpaceStatus.AVAILABLE)
                .build();

        ParkingSpace savedSpace = parkingSpaceRepository.save(parkingSpace);
        return mapToResponseDTO(savedSpace);
    }

    @Override
    public ParkingSpaceResponseDTO getSpaceById(Long id) {
        ParkingSpace space = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new ParkingSpaceNotFoundException("Parking space not found with ID: " + id));
        return mapToResponseDTO(space);
    }

    @Override
    public ParkingSpaceResponseDTO updateSpace(Long id, ParkingSpaceUpdateDTO dto) {
        ParkingSpace space = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new ParkingSpaceNotFoundException("Parking space not found with ID: " + id));

        space.setZone(dto.getZone());
        space.setCity(dto.getCity());
        space.setPricePerHour(dto.getPricePerHour());

        ParkingSpace updatedSpace = parkingSpaceRepository.save(space);
        return mapToResponseDTO(updatedSpace);
    }

    @Override
    public List<ParkingSpaceResponseDTO> searchSpaces(String city, String zone, Boolean available) {
        return parkingSpaceRepository.searchParkingSpaces(city, zone, available)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public ParkingSpaceResponseDTO reserveSpace(Long id) {
        ParkingSpace space = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new ParkingSpaceNotFoundException("Parking space not found with ID: " + id));

        if (space.getStatus() != ParkingSpaceStatus.AVAILABLE) {
            throw new SpaceNotAvailableException("Parking space with ID " + id + " is not available for reservation. Current status: " + space.getStatus());
        }

        space.setStatus(ParkingSpaceStatus.RESERVED);
        ParkingSpace updatedSpace = parkingSpaceRepository.save(space);
        return mapToResponseDTO(updatedSpace);
    }

    @Override
    public ParkingSpaceResponseDTO releaseSpace(Long id) {
        ParkingSpace space = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new ParkingSpaceNotFoundException("Parking space not found with ID: " + id));

        space.setStatus(ParkingSpaceStatus.AVAILABLE);
        ParkingSpace updatedSpace = parkingSpaceRepository.save(space);
        return mapToResponseDTO(updatedSpace);
    }

    @Override
    public ParkingSpaceResponseDTO updateStatus(Long id, ParkingSpaceStatus status) {
        ParkingSpace space = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new ParkingSpaceNotFoundException("Parking space not found with ID: " + id));

        if (status != null) {
            space.setStatus(status);
        }

        ParkingSpace updatedSpace = parkingSpaceRepository.save(space);
        return mapToResponseDTO(updatedSpace);
    }

    private ParkingSpaceResponseDTO mapToResponseDTO(ParkingSpace space) {
        return ParkingSpaceResponseDTO.builder()
                .id(space.getId())
                .zone(space.getZone())
                .city(space.getCity())
                .status(space.getStatus())
                .ownerId(space.getOwnerId())
                .pricePerHour(space.getPricePerHour())
                .build();
    }
}
