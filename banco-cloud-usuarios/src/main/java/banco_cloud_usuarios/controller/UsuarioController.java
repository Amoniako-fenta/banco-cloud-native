package banco_cloud_usuarios.controller;

import banco_cloud_usuarios.entity.Usuario;
import banco_cloud_usuarios.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerUsuarios(
            @RequestParam(required = false) Boolean autorizado) {

        if (autorizado != null) {

            if (autorizado) {
                return ResponseEntity.ok(
                        service.obtenerAutorizados()
                );
            }

            return ResponseEntity.ok(
                    service.obtenerPendientes()
            );
        }

        return ResponseEntity.ok(
                service.obtenerTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.obtenerPorId(id)
        );
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerPorEmail(
            @PathVariable String email) {

        return ResponseEntity.ok(
                service.obtenerPorEmail(email)
        );
    }

    @PostMapping
    public ResponseEntity<Usuario> registrar(
            @RequestBody Usuario usuario) {

        return ResponseEntity.ok(
                service.registrar(usuario)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {

        return ResponseEntity.ok(
                service.actualizar(id, usuario)
        );
    }

    @PutMapping("/{id}/autorizar")
    public ResponseEntity<Usuario> autorizar(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.autorizar(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(
            @PathVariable Long id) {

        service.desactivar(id);

        return ResponseEntity.noContent().build();
    }
}