package com.inventory.management.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String fullName,

        @Email(message = "Email inválido")
        @NotBlank(message = "El email es obligatorio")
        String email,

        @NotBlank(message = "El teléfono es obligatorio")
        String phone,

        @NotNull(message = "El rol es obligatorio")
        Long roleId,

        @NotNull(message = "La sucursal es obligatoria")
        Long branchId,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}