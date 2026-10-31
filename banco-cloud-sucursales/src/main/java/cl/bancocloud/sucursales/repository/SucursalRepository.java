package cl.bancocloud.sucursales.repository;

import cl.bancocloud.sucursales.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    List<Sucursal> findByComunaIgnoreCaseAndActivaTrue(String comuna);

    List<Sucursal> findByRegionIgnoreCaseAndActivaTrue(String region);

    List<Sucursal> findByActivaTrue();
}