package lk.ijse.spms.paymentservice.service;

import lk.ijse.spms.paymentservice.dto.PaymentChargeDTO;
import lk.ijse.spms.paymentservice.dto.TransactionResponseDTO;

import java.util.List;

public interface PaymentService {

    TransactionResponseDTO charge(PaymentChargeDTO dto);

    TransactionResponseDTO getReceipt(Long transactionId);

    List<TransactionResponseDTO> getTransactionsByUserId(Long userId);
}
