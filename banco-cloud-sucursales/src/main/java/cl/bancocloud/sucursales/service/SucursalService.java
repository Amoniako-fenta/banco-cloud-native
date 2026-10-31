package cl.bancocloud.sucursales.service;

import cl.bancocloud.sucursales.entity.Sucursal;
import cl.bancocloud.sucursales.rabbit.producer.SucursalEventProducer;
import cl.bancocloud.sucursales.repository.SucursalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService {

    private final SucursalRepository repository;

    private final SucursalEventProducer eventProducer;


    public SucursalService(
            SucursalRepository repository,
            SucursalEventProducer eventProducer
    ) {

        this.repository = repository;
        this.eventProducer = eventProducer;
    }


    public List<Sucursal> obtenerTodas() {

        return repository.findByActivaTrue();
    }


    public List<Sucursal> buscarPorComuna(
            String comuna
    ) {

        return repository
                .findByComunaIgnoreCaseAndActivaTrue(
                        comuna
                );
    }


    public List<Sucursal> buscarPorRegion(
            String region
    ) {

        return repository
                .findByRegionIgnoreCaseAndActivaTrue(
                        region
                );
    }


    public Sucursal obtenerPorId(
            Long id
    ) {

        return repository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Sucursal no encontrada"
                        )
                );
    }


    public Sucursal crear(
            Sucursal sucursal
    ) {

        sucursal.setActiva(true);


        Sucursal guardada =
                repository.save(sucursal);


        /*
         * Publicamos el evento
         * después de guardar
         * correctamente.
         */

        eventProducer
                .publicarSucursalCreada(
                        guardada
                );


        return guardada;
    }


    public Sucursal actualizar(
            Long id,
            Sucursal datos
    ) {

        Sucursal sucursal =
                obtenerPorId(id);


        sucursal.setNombre(
                datos.getNombre()
        );

        sucursal.setDireccion(
                datos.getDireccion()
        );

        sucursal.setComuna(
                datos.getComuna()
        );

        sucursal.setRegion(
                datos.getRegion()
        );

        sucursal.setHorarioApertura(
                datos.getHorarioApertura()
        );

        sucursal.setHorarioCierre(
                datos.getHorarioCierre()
        );


        return repository.save(
                sucursal
        );
    }


    public void desactivar(
            Long id
    ) {

        Sucursal sucursal =
                obtenerPorId(id);


        sucursal.setActiva(false);


        repository.save(
                sucursal
        );
    }
}