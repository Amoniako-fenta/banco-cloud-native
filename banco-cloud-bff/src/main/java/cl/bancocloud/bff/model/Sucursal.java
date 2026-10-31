package cl.bancocloud.bff.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la sucursal tal como la expone banco-cloud-sucursales.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {

    private Long id;
    private String nombre;
    private String direccion;
    private String comuna;
    private String region;
    private String horarioApertura;
    private String horarioCierre;
    private boolean activa;
}
