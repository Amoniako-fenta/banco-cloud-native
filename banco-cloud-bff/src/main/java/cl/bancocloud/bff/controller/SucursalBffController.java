package cl.bancocloud.bff.controller;

import cl.bancocloud.bff.client.SucursalClient;
import cl.bancocloud.bff.model.Sucursal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalBffController {

    private final SucursalClient sucursalClient;

    public SucursalBffController(SucursalClient sucursalClient) {
        this.sucursalClient = sucursalClient;
    }

    @GetMapping
    public ResponseEntity<List<Sucursal>> obtenerSucursales(
            @RequestParam(required = false) String comuna,
            @RequestParam(required = false) String region) {

        return ResponseEntity.ok(sucursalClient.obtenerSucursales(comuna, region));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(sucursalClient.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Sucursal> crear(@RequestBody Sucursal sucursal) {
        return ResponseEntity.ok(sucursalClient.crear(sucursal));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Sucursal> actualizar(@PathVariable Long id, @RequestBody Sucursal sucursal) {
        return ResponseEntity.ok(sucursalClient.actualizar(id, sucursal));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        sucursalClient.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
