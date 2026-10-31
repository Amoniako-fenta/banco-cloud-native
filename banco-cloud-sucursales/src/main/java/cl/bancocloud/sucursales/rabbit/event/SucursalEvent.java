package cl.bancocloud.sucursales.rabbit.event;

public record SucursalEvent(

        Long sucursalId,

        String nombre,

        String comuna,

        String region,

        String evento

) {
}