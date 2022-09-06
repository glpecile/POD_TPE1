package ar.edu.itba.pod.client.admin.actions;

/*
    models: Agrega una lote de modelos de aviones (ver detalle más abajo)
            flights: Agrega un lote de vuelos (ver detalle más abajo)
            status: Consulta el estado del vuelo de código flightCode. Deberá imprimir en pantalla el estado del vuelo luego de invocar a la acción o el error correspondiente
            confirm: Confirma el vuelo de código flightCode. Deberá imprimir en pantalla el estado del vuelo luego de invocar a la acción o el error correspondiente
            cancel: Cancela el vuelo de código flightCode. Deberá imprimir en pantalla el estado del vuelo luego de invocar a la acción o el error correspondiente
            reticketing: Fuerza el cambio de tickets de vuelos cancelados por tickets de vuelos alternativos
*/

public enum ActionType {
    MODELS,
    FLIGHTS,
    STATUS,
    CONFIRM,
    CANCEL,
    RETICKETING
}
