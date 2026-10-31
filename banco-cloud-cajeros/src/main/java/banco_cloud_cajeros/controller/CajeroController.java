package banco_cloud_cajeros.controller;

import banco_cloud_cajeros.entity.Cajero;
import banco_cloud_cajeros.service.CajeroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cajeros")
public class CajeroController {

    private final CajeroService service;

    public CajeroController(CajeroService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Cajero>> obtenerCajeros(
            @RequestParam(required = false) String comuna,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Long sucursalId) {

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

        if (sucursalId != null) {

            return ResponseEntity.ok(
                    service.buscarPorSucursal(sucursalId)
            );
        }

        return ResponseEntity.ok(
                service.obtenerTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cajero> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.obtenerPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<Cajero> crear(
            @RequestBody Cajero cajero) {

        return ResponseEntity.ok(
                service.crear(cajero)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cajero> actualizar(
            @PathVariable Long id,
            @RequestBody Cajero cajero) {

        return ResponseEntity.ok(
                service.actualizar(id, cajero)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(
            @PathVariable Long id) {

        service.desactivar(id);

        return ResponseEntity.noContent().build();
    }
}