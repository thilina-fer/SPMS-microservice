package lk.ijse.spms.paymentservice.controller;

import jakarta.validation.Valid;
import lk.ijse.spms.paymentservice.dto.PaymentChargeDTO;
import lk.ijse.spms.paymentservice.dto.ResponseDTO;
import lk.ijse.spms.paymentservice.dto.TransactionResponseDTO;
import lk.ijse.spms.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/charge")
    public ResponseEntity<ResponseDTO> charge(@Valid @RequestBody PaymentChargeDTO dto) {
        TransactionResponseDTO response = paymentService.charge(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), "Payment processed successfully", response));
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<ResponseDTO> getReceipt(@PathVariable Long id) {
        TransactionResponseDTO response = paymentService.getReceipt(id);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Receipt retrieved successfully", response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDTO> getTransactionsByUserId(@PathVariable Long userId) {
        List<TransactionResponseDTO> response = paymentService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "User transactions retrieved successfully", response));
    }
}
