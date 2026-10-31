package cl.bancocloud.sucursales.controller;

import cl.bancocloud.sucursales.entity.Sucursal;
import cl.bancocloud.sucursales.service.SucursalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {

    private final SucursalService service;

    public SucursalController(SucursalService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Sucursal>> obtenerSucursales(
            @RequestParam(required = false) String comuna,
            @RequestParam(required = false) String region) {

        if (comuna != null && !comuna.isBlank()) {
            return ResponseEntity.ok(
                    service.buscarPorComuna(comuna)
            );
        }

        if (region != null && !region.isBlank()) {
            return ResponseEntity.ok(
                    service.buscarPorRegion(region)
            );
        }

        return ResponseEntity.ok(
                service.obtenerTodas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.obtenerPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<Sucursal> crear(
            @RequestBody Sucursal sucursal) {

        return ResponseEntity.ok(
                service.crear(sucursal)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sucursal> actualizar(
            @PathVariable Long id,
            @RequestBody Sucursal sucursal) {

        return ResponseEntity.ok(
                service.actualizar(id, sucursal)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(
            @PathVariable Long id) {

        service.desactivar(id);

        return ResponseEntity.noContent().build();
    }
}