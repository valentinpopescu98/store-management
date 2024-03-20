package com.valentinpopescu98.storemanagement.registration;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/register")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService service;

    @PostMapping
    public @ResponseBody ResponseEntity<String> registerSubmit(@Valid @RequestBody RegistrationRequest request,
                                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getAllErrors().forEach(error ->
                    errorMessage.append(error.getDefaultMessage()).append(".\n"));

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorMessage.toString());
        }

        try {
            String message = service.registerSubmit(request);
            return ResponseEntity.ok(message);
        } catch (DataIntegrityViolationException | IllegalArgumentException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

}
