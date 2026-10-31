package banco_cloud_cajeros.repository;

import banco_cloud_cajeros.entity.Cajero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CajeroRepository extends JpaRepository<Cajero, Long> {

    List<Cajero> findByActivoTrue();

    List<Cajero> findByComunaIgnoreCaseAndActivoTrue(String comuna);

    List<Cajero> findByRegionIgnoreCaseAndActivoTrue(String region);

    List<Cajero> findBySucursalIdAndActivoTrue(Long sucursalId);
}