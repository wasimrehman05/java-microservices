package com.bankai.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User creation/update request")
public class UserRequest {

    @NotBlank(message = "Name is required")
    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    private String name;

    @Email(message = "Provide a valid email")
    @NotBlank(message = "Email is required")
    @Schema(description = "Email address of the user", example = "john.doe@example.com", required = true)
    private String email;

    @Schema(description = "Phone number of the user", example = "+1234567890")
    private String phone;
}
