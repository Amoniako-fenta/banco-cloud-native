package banco_cloud_usuarios.service;

import banco_cloud_usuarios.entity.Usuario;
import banco_cloud_usuarios.rabbit.producer.UsuarioEventProducer;
import banco_cloud_usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioEventProducer usuarioEventProducer;

    public UsuarioService(
            UsuarioRepository repository,
            UsuarioEventProducer usuarioEventProducer
    ) {
        this.repository = repository;
        this.usuarioEventProducer = usuarioEventProducer;
    }

    public List<Usuario> obtenerTodos() {
        return repository.findAll();
    }

    public List<Usuario> obtenerActivos() {
        return repository.findByActivoTrue();
    }

    public List<Usuario> obtenerAutorizados() {
        return repository.findByAutorizadoTrue();
    }

    public List<Usuario> obtenerPendientes() {
        return repository.findByAutorizadoFalse();
    }

    public Usuario obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));
    }

    public Usuario obtenerPorEmail(String email) {
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));
    }

    public Usuario registrar(Usuario usuario) {

        usuario.setActivo(true);
        usuario.setAutorizado(false);

        if (usuario.getRol() == null ||
                usuario.getRol().isBlank()) {

            usuario.setRol("CLIENTE");
        }

        Usuario guardado = repository.save(usuario);

        usuarioEventProducer.publicarUsuarioCreado(
                guardado
        );

        return guardado;
    }

    public Usuario actualizar(
            Long id,
            Usuario datos
    ) {

        Usuario usuario = obtenerPorId(id);

        usuario.setNombre(datos.getNombre());
        usuario.setEmail(datos.getEmail());

        return repository.save(usuario);
    }

    public Usuario autorizar(Long id) {

        Usuario usuario = obtenerPorId(id);

        usuario.setAutorizado(true);

        return repository.save(usuario);
    }

    public void desactivar(Long id) {

        Usuario usuario = obtenerPorId(id);

        usuario.setActivo(false);

        repository.save(usuario);
    }
}
