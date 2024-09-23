package com.joel.gestion_pedidos.model.rdbms;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table("cliente")
public class ClienteEntity implements Serializable{
    
    @Id
    private Long id;

    @NotNull
	@NotBlank
	@Size(min = 4, max = 12)
    private String nombre;

    @NotNull
	@NotBlank
    @Email
    private String email;

    private String direccion;

    @Size(min = 7, max = 9)
    private String telefono;

    @NotNull
	@NotBlank
    @Size(min = 6, max = 8)
    private String password;
}
