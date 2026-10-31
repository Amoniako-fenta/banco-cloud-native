package cl.bancocloud.bff.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa el usuario tal como lo expone banco-cloud-usuarios.
 * Usa Boolean (no boolean) para no enviar "false" por defecto en el autoregistro.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Long id;
    private String nombre;
    private String email;
    private Boolean activo;
    private Boolean autorizado;
    private String rol;
}
