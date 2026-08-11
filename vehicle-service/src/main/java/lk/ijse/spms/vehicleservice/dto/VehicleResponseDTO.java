package lk.ijse.spms.vehicleservice.dto;

import lk.ijse.spms.vehicleservice.entity.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponseDTO {
    private Long id;
    private String plateNumber;
    private String type;
    private Long userId;
    private VehicleStatus status;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
}
