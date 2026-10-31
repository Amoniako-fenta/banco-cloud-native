package banco_cloud_usuarios.rabbit.event;

public record UsuarioEvent(
        Long usuarioId,
        String nombre,
        String email,
        Boolean autorizado,
        String rol,
        String evento
) {
}