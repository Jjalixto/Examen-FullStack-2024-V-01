package com.joel.gestion_pedidos.model.rdbms;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table("producto")
public class ProductoEntity implements Serializable{
    
    @Id
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
}
