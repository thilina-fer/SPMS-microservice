package lk.ijse.spms.parkingspaceservice.dto;

import jakarta.validation.constraints.NotNull;
import lk.ijse.spms.parkingspaceservice.entity.ParkingSpaceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateDTO {

    @NotNull(message = "Status is required")
    private ParkingSpaceStatus status;
}
