package banco_cloud_usuarios.repository;

import banco_cloud_usuarios.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    List<Usuario> findByAutorizadoTrue();

    List<Usuario> findByAutorizadoFalse();

    List<Usuario> findByActivoTrue();
}