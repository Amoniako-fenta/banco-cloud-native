package cl.bancocloud.bff.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa el cajero automático tal como lo expone banco-cloud-cajeros.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cajero {

    private Long id;
    private String nombre;
    private String direccion;
    private String comuna;
    private String region;
    private String horarioApertura;
    private String horarioCierre;
    private boolean activo;
    private Long sucursalId;
}
