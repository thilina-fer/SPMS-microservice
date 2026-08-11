package lk.ijse.spms.parkingspaceservice.exception;

import lk.ijse.spms.parkingspaceservice.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ParkingSpaceNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleParkingSpaceNotFoundException(ParkingSpaceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null));
    }

    @ExceptionHandler(SpaceNotAvailableException.class)
    public ResponseEntity<ResponseDTO> handleSpaceNotAvailableException(SpaceNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
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
