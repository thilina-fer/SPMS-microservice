package lk.ijse.spms.paymentservice.dto;

import lk.ijse.spms.paymentservice.entity.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    private Long id;
    private Long userId;
    private Long vehicleId;
    private Long parkingSpaceId;
    private BigDecimal amount;
    private TransactionStatus status;
    private String receiptCode;
    private LocalDateTime timestamp;
}
