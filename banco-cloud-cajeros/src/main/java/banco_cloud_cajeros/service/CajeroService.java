package banco_cloud_cajeros.service;

import banco_cloud_cajeros.entity.Cajero;
import banco_cloud_cajeros.rabbit.producer.CajeroEventProducer;
import banco_cloud_cajeros.repository.CajeroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CajeroService {

    private final CajeroRepository repository;

    private final CajeroEventProducer eventProducer;


    public CajeroService(
            CajeroRepository repository,
            CajeroEventProducer eventProducer
    ) {

        this.repository = repository;
        this.eventProducer = eventProducer;
    }


    public List<Cajero> obtenerTodos() {

        return repository.findByActivoTrue();
    }


    public List<Cajero> buscarPorComuna(
            String comuna
    ) {

        return repository
                .findByComunaIgnoreCaseAndActivoTrue(
                        comuna
                );
    }


    public List<Cajero> buscarPorRegion(
            String region
    ) {

        return repository
                .findByRegionIgnoreCaseAndActivoTrue(
                        region
                );
    }


    public List<Cajero> buscarPorSucursal(
            Long sucursalId
    ) {

        return repository
                .findBySucursalIdAndActivoTrue(
                        sucursalId
                );
    }


    public Cajero obtenerPorId(
            Long id
    ) {

        return repository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Cajero no encontrado"
                        )
                );
    }


    public Cajero crear(
            Cajero cajero
    ) {

        cajero.setActivo(true);


        Cajero guardado =
                repository.save(cajero);


        /*
         * Después de guardar correctamente
         * publicamos el evento.
         */

        eventProducer
                .publicarCajeroCreado(
                        guardado
                );


        return guardado;
    }


    public Cajero actualizar(
            Long id,
            Cajero datos
    ) {

        Cajero cajero =
                obtenerPorId(id);


        cajero.setNombre(
                datos.getNombre()
        );

        cajero.setDireccion(
                datos.getDireccion()
        );

        cajero.setComuna(
                datos.getComuna()
        );

        cajero.setRegion(
                datos.getRegion()
        );

        cajero.setHorarioApertura(
                datos.getHorarioApertura()
        );

        cajero.setHorarioCierre(
                datos.getHorarioCierre()
        );

        cajero.setSucursalId(
                datos.getSucursalId()
        );


        return repository.save(
                cajero
        );
    }


    public void desactivar(
            Long id
    ) {

        Cajero cajero =
                obtenerPorId(id);


        cajero.setActivo(false);


        repository.save(
                cajero
        );
    }
}