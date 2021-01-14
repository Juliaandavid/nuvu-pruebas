# Prueba nuvu

## Primeros pasos
Para comenzar se realizó una revisión de los requerimientos especificados en el documento los cuales eran:

1. Desarollar un API (REST/SOAP)
2. Crear, obtener y actualizar un usuario
3. Crear, obtener, actualizar y eliminar su tarjeta de crédito
4. Debe tener servicio de autenticación

### Modelo
Para comenzar el desarollo realicé un modelo de datos para almacenar la información.

| User |
| ------- |
| documentId |
| firstName |
| lastName |

| CreditCard |
| ------- |
| userId 1:1 User  |
| token |
| lastFour |
| monthExpiration |
| yearExpiration |
| cvc |

De acuerdo con el requerimiento un usuario sólo puede tener una tarjeta de crédito, también se realiza una encriptación
del número de la tarjeta, y se guardan los últimos 4 digitos para la referencia del usuario.

### Repositorios
Luego realicé la creación de los repositorios DAO para cada entidad. 

### Controladores
Una vez los repositorios estan listos y se realiza la creación de los controladores, dado que en los controladores hay diferentes
queries se completan en los repositorios.

### Seguridad
Para la seguridad del API REST se implemento un sistema JWT, donde la sesión de Spring es STATELESS.

Dado que la información es bancaria, se realiza un filtro de seguridad para un usuario sólo pueda leer y escribir su propia información
de lo contrario se deniega el permiso para acceder a otro usuario.

### Test
Debido al corto tiempo que tuve para realizar la prueba, no codifiqué test unitarios para el REST API, sin embargo ejecute un
suitcase con un servicio Http y el resultado fue satisfactorio.
