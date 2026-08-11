package lk.ijse.spms.parkingspaceservice.dto;

import lk.ijse.spms.parkingspaceservice.entity.ParkingSpaceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpaceResponseDTO {
    private Long id;
    private String zone;
    private String city;
    private ParkingSpaceStatus status;
    private Long ownerId;
    private BigDecimal pricePerHour;
}
