package cl.bancocloud.bff.controller;

import cl.bancocloud.bff.client.CajeroClient;
import cl.bancocloud.bff.model.Cajero;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cajeros")
public class CajeroBffController {

    private final CajeroClient cajeroClient;

    public CajeroBffController(CajeroClient cajeroClient) {
        this.cajeroClient = cajeroClient;
    }

    @GetMapping
    public ResponseEntity<List<Cajero>> obtenerCajeros(
            @RequestParam(required = false) String comuna,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Long sucursalId) {

        return ResponseEntity.ok(cajeroClient.obtenerCajeros(comuna, region, sucursalId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cajero> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cajeroClient.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cajero> crear(@RequestBody Cajero cajero) {
        return ResponseEntity.ok(cajeroClient.crear(cajero));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cajero> actualizar(@PathVariable Long id, @RequestBody Cajero cajero) {
        return ResponseEntity.ok(cajeroClient.actualizar(id, cajero));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        cajeroClient.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
