package lk.ijse.spms.paymentservice.service.impl;

import lk.ijse.spms.paymentservice.dto.PaymentChargeDTO;
import lk.ijse.spms.paymentservice.dto.TransactionResponseDTO;
import lk.ijse.spms.paymentservice.entity.Transaction;
import lk.ijse.spms.paymentservice.entity.TransactionStatus;
import lk.ijse.spms.paymentservice.exception.PaymentFailedException;
import lk.ijse.spms.paymentservice.exception.TransactionNotFoundException;
import lk.ijse.spms.paymentservice.repository.TransactionRepository;
import lk.ijse.spms.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final TransactionRepository transactionRepository;

    @Override
    public TransactionResponseDTO charge(PaymentChargeDTO dto) {
        boolean isValidCard = validateCardNumberWithLuhn(dto.getCardNumber());

        if (!isValidCard) {
            Transaction failedTransaction = Transaction.builder()
                    .userId(dto.getUserId())
                    .vehicleId(dto.getVehicleId())
                    .parkingSpaceId(dto.getParkingSpaceId())
                    .amount(dto.getAmount())
                    .status(TransactionStatus.FAILED)
                    .receiptCode(null)
                    .timestamp(LocalDateTime.now())
                    .build();

            transactionRepository.save(failedTransaction);
            throw new PaymentFailedException("Payment failed: Invalid card number (Luhn check failed)");
        }

        String receiptCode = "RCPT-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();

        Transaction transaction = Transaction.builder()
                .userId(dto.getUserId())
                .vehicleId(dto.getVehicleId())
                .parkingSpaceId(dto.getParkingSpaceId())
                .amount(dto.getAmount())
                .status(TransactionStatus.SUCCESS)
                .receiptCode(receiptCode)
                .timestamp(LocalDateTime.now())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToResponseDTO(savedTransaction);
    }

    @Override
    public TransactionResponseDTO getReceipt(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + transactionId));
        return mapToResponseDTO(transaction);
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private boolean validateCardNumberWithLuhn(String cardNumber) {
        if (cardNumber == null) {
            return false;
        }
        String digits = cardNumber.replaceAll("[\\s-]+", "");
        if (digits.isEmpty() || !digits.matches("\\d{13,19}")) {
            return false;
        }

        int sum = 0;
        boolean alternate = false;
        for (int i = digits.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(digits.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    private TransactionResponseDTO mapToResponseDTO(Transaction transaction) {
        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .vehicleId(transaction.getVehicleId())
                .parkingSpaceId(transaction.getParkingSpaceId())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .receiptCode(transaction.getReceiptCode())
                .timestamp(transaction.getTimestamp())
                .build();
    }
}
