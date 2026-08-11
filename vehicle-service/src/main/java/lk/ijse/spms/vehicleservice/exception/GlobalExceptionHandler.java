package lk.ijse.spms.vehicleservice.exception;

import lk.ijse.spms.vehicleservice.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleVehicleNotFoundException(VehicleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null));
    }

    @ExceptionHandler(PlateNumberAlreadyExistsException.class)
    public ResponseEntity<ResponseDTO> handlePlateNumberAlreadyExistsException(PlateNumberAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResponseDTO(HttpStatus.CONFLICT.value(), ex.getMessage(), null));
    }

    @ExceptionHandler(InvalidVehicleStateException.class)
    public ResponseEntity<ResponseDTO> handleInvalidVehicleStateException(InvalidVehicleStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), errorMessage, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null));
    }
}
