# Sistema Hotelero

### Descripción

Sistema Hotelero es una API REST desarrollada con Java y Spring Boot para la administración integral de hoteles. Permite gestionar huéspedes, habitaciones, reservas, consumos, pagos, check-in, check-out y usuarios, incorporando autenticación JWT y control de acceso basado en roles.

### Integrantes
Francisco Mandolino,

Mauro Massi,

Joaquín Ortiz Martínez.

### Tecnologías Utilizadas

- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security
- JWT
- MySQL
- Maven
- Lombok
- Swagger / OpenAPI
- Jakarta Validation

### Arquitectura

El proyecto implementa una arquitectura en capas:

Controllers -
Services -
Repositories -
DTOs -
Entities -
Security 

Esta estructura favorece la separación de responsabilidades y facilita el mantenimiento del sistema.

### Estructura del Proyecto
```text
src/main/java
└── lpda.SistemaHotelero
├── config
├── exceptions
├── seguridad
├── features
│   ├── acompanantes
│   ├── canalesReservas
│   ├── checkIn
│   ├── checkOut
│   ├── consumos
│   ├── habitaciones
│   ├── huespedes
│   ├── pagos
│   ├── permisos
│   ├── productos
│   ├── reservas
│   ├── roles
│   └── usuarios
└── SistemaHoteleroApplication
```

#### Nota

El proyecto está organizado por módulos funcionales
dentro del paquete features. Cada módulo contiene 
sus propias entidades, DTOs, repositorios, servicios 
y controladores, favoreciendo una arquitectura modular 
y escalable.

### Funcionalidades Principales

Gestión de huéspedes,
Gestión de habitaciones,
Gestión de reservas,
Gestion de consumos,
Gestión de pagos,
Registro de check-in,
Registro de check-out,
Administración de usuarios,
Gestión de roles y permisos,
Seguridad mediante JWT.

### Configuración - Requisitos
JDK 21,
Maven,
MySQL.

### Seguridad
Autenticación mediante JWT.
Control de acceso basado en roles y permisos.
Arquitectura stateless.
Protección de endpoints mediante Spring Security.

### Ejecución
Clonar el repositorio.
Configurar la base de datos MySQL.
Ajustar las credenciales en application.properties.
Ejecutar:
mvn spring-boot:run
Documentación API

## Endpoints Principales


### Autenticación

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| POST | `/auth/login` | Autenticar usuario y generar JWT |

---

### Huéspedes

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| GET | `/api/huespedes` | Listar huéspedes |
| GET | `/api/huespedes/{idExterno}` | Buscar huésped |
| POST | `/api/huespedes` | Crear huésped |
| DELETE | `/api/huespedes/{idExterno}` | Eliminar huésped |

---
### Consumos

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| POST | `/api/consumos` | Registrar consumo |
| GET | `/api/consumos` | Listar consumos |
| GET | `/api/consumos/{idExterno}` | Buscar consumo |
| GET | `/api/consumos/reserva/{idReservaExterno}` | Listar consumos por reserva |
| DELETE | `/api/consumos/{idExterno}` | Eliminar consumo |
---
### Habitaciones

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| GET | `/api/habitaciones` | Listar habitaciones |
| GET | `/api/habitaciones/{id}` | Buscar habitación |
| POST | `/api/habitaciones` | Crear habitación |
| PUT | `/api/habitaciones/{id}` | Modificar habitación |
| DELETE | `/api/habitaciones/{id}` | Eliminar habitación |
| PATCH | `/api/habitaciones/{id}/estado-ocupacion` | Cambiar ocupación |
| PATCH | `/api/habitaciones/limpieza/{numero}/estado-limpieza` | Actualizar limpieza |

---

### Reservas

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| POST | `/api/reservas` | Crear reserva |
| GET | `/api/reservas` | Listar reservas |
| GET | `/api/reservas/{id}` | Buscar reserva |
| PUT | `/api/reservas/{id}` | Modificar reserva |
| DELETE | `/api/reservas/{id}` | Cancelar reserva |

---

### Pagos

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| POST | `/pagos` | Registrar pago |
| GET | `/pagos` | Listar pagos |
| GET | `/pagos/{idExterno}` | Buscar pago |

---

### Check-In

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| POST | `/api/checkIn` | Registrar ingreso |
| GET | `/api/checkIn/{idExterno}` | Consultar check-in |

---

### Check-Out

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| POST | `/api/checkOut` | Registrar salida |
| GET | `/api/checkOut/{idExterno}` | Consultar check-out |

---
## Documentación Completa

La documentación completa de la API se encuentra disponible mediante Swagger/OpenAPI:


http://localhost:8080/swagger-ui/index.html

---
### Procesos Críticos del Negocio
- Creación de reservas.
- Registro de pagos.
- Check-in de huéspedes.
- Check-out de huéspedes.
- Gestión de ocupación y limpieza de habitaciones.
- Autenticación y autorización mediante JWT.
---
### Trabajo Académico

Proyecto desarrollado para la materia Programación III – UTN Facultad Regional Mar del Plata.