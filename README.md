# TPE1-POD-G3

- [TPE1-POD-G3](#tpe1-pod-g3)
  - [Autores](#autores)
  - [Compilación](#compilación)
  - [Ejecución](#ejecución)
    - [Server](#server)
    - [Clientes](#clientes)
      - [Admin Client](#admin-client)
      - [Notification Client](#notification-client)
      - [SeatMapClient](#seatmapclient)
      - [SeatAssignmentClient](#seatassignmentclient)

## Autores

- [Arce, Julián Francisco](https://github.com/JuArce)
- [Catalán, Roberto José](https://github.com/rcatalan98)
- [Dell'Isola, Lucas](https://github.com/ldellisola)
- [Pecile, Gian Luca](https://github.com/glpecile)
- [Torrusio, Lucía](https://github.com/luciatorrusio)

## Compilación

Para compilar el proyecto, se debe ejecutar el siguiente comando en la raíz del proyecto:

```bash
mvn clean install
```

## Ejecución

Para la ejecución del proyecto, es necesario ejecutar los siguientes comandos:

```bash
cd scripts
./run-rmi
```

Luego, en otra terminal, ejecutar:

```bash
cd scripts
./run-[server | admin | notifications | seatMap | seatAssign] <params>
```

Donde se tiene:

- [`server`](#server) para ejecutar el servidor.
- [`admin`](#admin-client) para ejecutar el cliente de administración.
- [`notifications`](#notification-client) para ejecutar el cliente de notificaciones.
- [`seatMap`](#seatassignmentclient) para ejecutar el cliente de mapa de asientos.
- [`seatAssign`](#seatassignmentclient) para ejecutar el cliente de asignación de asientos.

A continuación se detallan las funcionalidades y ejecución de los clientes y server.

### Server

El servidor toma un parámetro opcional `-p` que indica el puerto en el que se ejecutará el servidor.

Por defecto el puerto es `1099`.

### Clientes

A continuación se detallan los clientes y sus funcionalidades.

#### Admin Client

```sh
./run-admin -DserverAddress=xx.xx.xx.xx:yyyy -Daction=actionName [ -DinPath=filename | -Dflight=flightCode ]
```

| Opción     | Opciones                                                           | Descripción                             |
| ---------- | ------------------------------------------------------------------ | --------------------------------------- |
| `-Daction` | `models`, `flights`, `status`, `confirm`, `cancel` y `reticketing` | Acción a realizar del cliente de admin. |
| `-DinPath` | \[OPCIONAL\] `String filename`                                     | Ruta del archivo de entrada.            |
| `-Dflight` | \[OPCIONAL\] `String flightCode`                                   | Código del vuelo.                       |

#### Notification Client

```sh
./run-notifcations -DserverAddress=xx.xx.xx.xx:yyyy -Dflight=flightCode -Dpassenger=name
```

| Opción        | Opciones            | Descripción          |
| ------------- | ------------------- | -------------------- |
| `-Dflight`    | `String flightCode` | Código del vuelo.    |
| `-Dpassenger` | `String name`       | Nombre del pasajero. |

#### SeatMapClient

```sh
./run-seatMap -DserverAddress=xx.xx.xx.xx:yyyy -Dflight=flightCode [ -Dcategory=catName | -Drow=rowNumber ] -DoutPath=output.csv
```

| Opción       | Opciones                                                | Descripción                 |
| ------------ | ------------------------------------------------------- | --------------------------- |
| `-Dflight`   | `String flightCode`                                     | Código del vuelo.           |
| `-Dcategory` | \[OPCIONAL\] `(BUSINESS \| PREMIUM_ECONOMY \| ECONOMY)` | Nombre de la categoría.     |
| `-Drow`      | \[OPCIONAL\] `int rowNumber`                            | Número de fila.             |
| `-DoutPath`  | `String output.csv`                                     | Ruta del archivo de salida. |

#### SeatAssignmentClient

```sh
./run-seatAssign -DserverAddress=xx.xx.xx.xx:yyyy -Daction=actionName -Dflight=flightCode [ -Dpassenger=name | -Drow=num | -Dcol=L | -DoriginalFlight=originFlightCode ]
```

| Opción        | Opciones                                                    | Descripción                             |
| ------------- | ----------------------------------------------------------- | --------------------------------------- |
| `-Daction`    | `assign`, `status`, `move`, `alternatives` y `changeTicket` | Acción a realizar del cliente de admin. |
| `-Dflight`    | `String flightCode`                                         | Código del vuelo.                       |
| `-Dpassenger` | \[OPCIONAL\] `String name`                                  | Nombre del pasajero.                    |
| `-Drow`       | \[OPCIONAL\] `int rowNumber`                                | Número de fila.                         |
| `-Dcol`       | \[OPCIONAL\] `char col`                                     | Letra de columna.                       |

------------------------------------------------------------------------


# Correcciones

## Destacado:

* La claridad del informe, bien detalladas y justificadas las decisiones.
* La cantidad y calidad de tests.
* El diseño y uso de patrones a la hora de desarrollar tanto cliente como servidor.
 
## Oportunidad de Mejora:

* Cuidado con filtrar lógica del servidor en la api por ejemplo Flight, AlternativeFlight tienen lógica.
* Flight.getFreeSeatsInCategory: El primer ciclo parece innecesario.
* Es buena práctica notificar en un thread para no bloquear a quienes llaman al servicio esperando el retorno de los Handlers
* `t.getSeatLocation().isPresent()` -> `ticket.isAssigned()` ?
* changeTicket calcula todas las alternativas para luego buscar el vuelo pedido. Para no recorrer los vuelos se podría pedir el vuelo seleccionado y chequear si es alternativo directamente.
 

## Errores:

* En la mayoría de los métodos del servicio de asignación se chequea el estado del vuelo dos veces (diferentes estados), esto abre la posibilidad de que cambie entre chequeo y chequeo. 
* AdminServiceImpl. addPlane tiene una condición de carrera entre que chequea que el modelo está en la colección y setea el nuevo. Una alternativa sería sincronizar el llamado a un put if absent y luego a partir de la respuesta ver si ya existía o si se insertó.
* La acción “flights” no debe abortar cuando no se puede agregar uno de los vuelos del lote. Se pedía de imprimir una salida como “Cannot add flight” y “X flights added” y en su lugar se obtiene el stacktrace de una excepción PlaneModelNotExistException: The plane model does not exist. El catch que hace en ar.edu.itba.pod.client.admin.actions.FlightsAction#run captura RemoteException pero no PlaneModelNotExistException.
* En cliente de consulta de mapa de asientos, se pedía que no se genere el CSV cuando no hay asientos que cumplen el criterio indicado, y en cambio genera un CSV vacío.
* Ante un error en el cliente de notificaciones, el mismo debería finalizar su ejecución. El cliente se mantiene en ejecución porque quedó exportado. 
* El cliente de notificaciones permite registrar a un pasajero para que sea notificado de un vuelo confirmado y no corresponde.
* La acción “reticketing” imprime en salida el mensaje “Error while reticketing” y no corresponde. Si un ticket no se puede cambiar, se debe seguir intentando con los demás. Se pedía que se imprima la cantidad de tickets cambiados y se listen los que no se pudieron cambiar.
