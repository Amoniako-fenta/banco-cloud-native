package banco_cloud_cajeros.rabbit.event;

public record CajeroEvent(

        Long cajeroId,

        String nombre,

        String comuna,

        String region,

        Long sucursalId,

        String evento

) {
}