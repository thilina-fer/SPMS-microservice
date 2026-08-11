package lk.ijse.spms.parkingspaceservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpaceUpdateDTO {

    @NotBlank(message = "Zone/location is required")
    @JsonAlias({"location", "zone"})
    private String zone;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Price per hour is required")
    @Positive(message = "Price per hour must be positive")
    private BigDecimal pricePerHour;
}
