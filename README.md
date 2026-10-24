# Sistema Hotelero

### Descripción

Sistema Hotelero es una aplicación web para la administración integral de hoteles. Permite gestionar huéspedes, habitaciones, reservas, consumos, pagos, check-in, check-out y usuarios, con autenticación JWT y control de acceso basado en roles.

El repositorio contiene las dos partes del sistema:

- **back-end:** API REST desarrollada con Java y Spring Boot (Programación III).
- **front-end:** aplicación web desarrollada con Angular y TypeScript (Programación IV).

---
### Integrantes
Francisco Mandolino,

Mauro Massi,

Joaquín Ortiz Martínez,

Gonzalo Gavilondo.


---
### Tecnologías Utilizadas

#### Back-end
- Java 21
- Spring Boot 4
- Spring MVC
- Spring Data JPA
- Spring Security
- JWT
- MySQL
- Maven
- Lombok
- Swagger / OpenAPI
- Jakarta Validation

#### Front-end
- Angular 22 (componentes standalone, signals, zoneless)
- TypeScript
- HTML y CSS
- RxJS
- HttpClient + interceptors
- Formularios reactivos

---
### Estructura del Repositorio
```text
SistemaHotelero/
├── back-end/      API REST con Spring Boot
└── front-end/     Aplicación web con Angular
```

---
### Arquitectura

#### Back-end
Arquitectura en capas:

Controllers -
Services -
Repositories -
DTOs -
Entities -
Security

```text
back-end/src/main/java
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
│   ├── dashboard
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

El proyecto está organizado por módulos funcionales dentro del paquete `features`. Cada módulo contiene sus propias entidades, DTOs, repositorios, servicios y controladores, favoreciendo una arquitectura modular y escalable.

#### Front-end
Organizado en tres carpetas principales:

```text
front-end/src/app
├── core        Lo que se instancia una única vez y usa toda la app
│               (servicio base Api, autenticación, guards, interceptors, layout)
├── features    Un módulo por funcionalidad, igual que en el back-end
│               (cada uno con pages, components, services y models)
└── shared      Componentes, modelos, pipes y utilidades compartidos
```

- **Page:** componente con una ruta asociada.
- **Component:** componente de presentación, sin ruta; recibe datos con `input()` y avisa con `output()`.
- Los servicios de cada feature usan el servicio base `Api` (`core/services/api.ts`), que es el único que conoce la URL del backend.
- Los errores HTTP se traducen a mensajes entendibles en `core/interceptors/error-interceptor.ts`.

---
### Funcionalidades Principales

Gestión de huéspedes,
Gestión de habitaciones,
Gestión de reservas,
Gestión de acompañantes,
Gestión de consumos y productos,
Gestión de pagos,
Registro de check-in,
Registro de check-out,
Dashboard con resumen del hotel,
Administración de usuarios,
Gestión de roles y permisos,
Seguridad mediante JWT.

---
### Configuración - Requisitos
JDK 21,
Maven (incluido con `mvnw`),
MySQL,
Node.js 22.22.3+ o 24.15.0+,
Angular CLI 22.

---
### Seguridad
Autenticación mediante JWT.
Control de acceso basado en roles (ADMIN, RECEPCIONISTA, LIMPIEZA) y permisos.
Arquitectura stateless.
Protección de endpoints mediante Spring Security.
CORS habilitado para el front-end en `http://localhost:4200`.

---
### Ejecución

#### 1. Back-end
1. Clonar el repositorio.
2. Crear la base de datos en MySQL.
3. Definir las variables de entorno que usa `back-end/src/main/resources/application.yaml`:

   | Variable | Ejemplo |
   |----------|---------|
   | `DB_URL` | `jdbc:mysql://localhost:3306/sistema_hotelero` |
   | `DB_USER` | `root` |
   | `DB_PASSWORD` | `tu_contraseña` |
   | `JWT_SECRET` | clave secreta para firmar los tokens |

   En IntelliJ: **Run → Edit Configurations → Environment variables**.
4. Ejecutar desde la carpeta `back-end`:
   ```bash
   cd back-end
   ./mvnw spring-boot:run
   ```
   La API queda disponible en `http://localhost:8080`.

En IntelliJ hay que abrir la carpeta `back-end` (no la raíz del repositorio) para que reconozca el proyecto Maven.

#### 2. Front-end
```bash
cd front-end
npm install
ng serve
```
La aplicación queda disponible en `http://localhost:4200`. La URL del backend se configura en `front-end/src/environments/`.

---
## Endpoints Principales

Los endpoints protegidos requieren el header `Authorization: Bearer <token>`, obtenido en `/auth/login`.

### Autenticación

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| POST | `/auth/login` | Autenticar usuario y generar JWT |

---
### Usuarios

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| GET | `/api/usuarios` | Listar usuarios |
| POST | `/api/usuarios` | Crear usuario |
| PATCH | `/api/usuarios/{idExterno}/activo` | Cambiar estado de usuario |
| DELETE | `/api/usuarios/{idExterno}` | Eliminar usuario |

---
### Huéspedes

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| GET | `/api/huespedes` | Buscar huéspedes |
| GET | `/api/huespedes/{idExterno}` | Buscar huésped por ID |
| POST | `/api/huespedes` | Registrar huésped |
| DELETE | `/api/huespedes/{idExterno}` | Eliminar huésped |

---
### Habitaciones

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| GET | `/api/habitaciones` | Listar habitaciones |
| GET | `/api/habitaciones/{idExterno}` | Buscar habitación |
| GET | `/api/habitaciones/filtrar` | Filtrar habitaciones |
| GET | `/api/habitaciones/disponibles-por-fecha` | Consultar disponibilidad por fechas |
| GET | `/api/habitaciones/limpieza` | Listado para limpieza |
| POST | `/api/habitaciones` | Crear habitación |
| PUT | `/api/habitaciones/{idExterno}` | Actualizar habitación |
| PATCH | `/api/habitaciones/{idExterno}` | Actualizar parcialmente habitación |
| PATCH | `/api/habitaciones/{idExterno}/estado-ocupacion` | Cambiar estado de ocupación |
| PATCH | `/api/habitaciones/{idExterno}/activa` | Activar o desactivar habitación |
| PATCH | `/api/habitaciones/limpieza/{numero}/estado-limpieza` | Cambiar estado de limpieza |
| DELETE | `/api/habitaciones/{idExterno}` | Eliminar habitación |

---
### Reservas

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| GET | `/api/reservas` | Listar reservas |
| GET | `/api/reservas/{id}` | Buscar reserva por ID |
| GET | `/api/reservas/confirmadas` | Reservas confirmadas |
| GET | `/api/reservas/en-curso` | Reservas en curso |
| GET | `/api/reservas/finalizadas` | Reservas finalizadas |
| GET | `/api/reservas/canceladas` | Reservas canceladas |
| POST | `/api/reservas` | Crear reserva |
| PUT | `/api/reservas/{id}` | Actualizar reserva |
| PATCH | `/api/reservas/{id}/cancelar` | Cancelar reserva |
| DELETE | `/api/reservas/{id}` | Eliminar reserva |

---
### Canales de Reserva

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| GET | `/canalesReserva` | Listar canales de reserva |
| GET | `/canalesReserva/{id}` | Buscar canal por ID |
| POST | `/canalesReserva` | Crear canal de reserva |
| PUT | `/canalesReserva/{id}` | Actualizar canal de reserva |
| DELETE | `/canalesReserva/{id}` | Eliminar canal de reserva |

---
### Acompañantes

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| GET | `/api/acompanantes` | Listar acompañantes |
| GET | `/api/acompanantes/{idExterno}` | Buscar acompañante por ID |
| GET | `/api/acompanantes/reserva/{idExterno}` | Listar acompañantes por reserva |
| POST | `/api/acompanantes` | Registrar acompañante |
| PUT | `/api/acompanantes/{idExterno}` | Actualizar acompañante |
| DELETE | `/api/acompanantes/{idExterno}` | Eliminar acompañante |

---
### Productos

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| GET | `/api/productos` | Listar productos |
| GET | `/api/productos/{idExterno}` | Buscar producto por ID |
| POST | `/api/productos` | Crear producto |
| PUT | `/api/productos/{idExterno}` | Actualizar producto |
| PATCH | `/api/productos/{idExterno}/activo` | Cambiar estado de producto |
| DELETE | `/api/productos/{idExterno}` | Eliminar producto |

---
### Consumos

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| GET | `/api/consumos` | Listar consumos |
| GET | `/api/consumos/{idExterno}` | Buscar consumo por ID |
| GET | `/api/consumos/hoy` | Listar consumos del día |
| GET | `/api/consumos/reserva/{idReservaExterno}` | Listar consumos por reserva |
| POST | `/api/consumos` | Registrar consumo |
| DELETE | `/api/consumos/{idExterno}` | Eliminar consumo |

---
### Pagos

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| GET | `/pagos` | Listar pagos |
| GET | `/pagos/{idExterno}` | Buscar pago por ID |
| GET | `/pagos/reserva/{idReservaExterno}` | Listar pagos por reserva |
| GET | `/pagos/usuario/{idUsuario}` | Listar pagos por usuario |
| POST | `/pagos` | Registrar pago |

---
### Check-In

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| POST | `/api/checkIn` | Registrar check-in |
| GET | `/api/checkIn/{idExterno}` | Buscar check-in por ID |
| DELETE | `/api/checkIn/{idExterno}` | Eliminar check-in |

---
### Check-Out

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| POST | `/api/checkOut` | Registrar check-out |
| GET | `/api/checkOut/{idExterno}` | Buscar check-out por ID |
| GET | `/api/checkOut/resumen/{idReservaExterno}` | Obtener resumen de check-out |
| DELETE | `/api/checkOut/{idExterno}` | Eliminar check-out |

---
### Dashboard

| Método | Endpoint | Descripción |
|---------|----------|-------------|
| GET | `/api/dashboard/resumen` | Obtener resumen del dashboard |

---
## Documentación Completa

La documentación completa de la API se encuentra disponible mediante Swagger/OpenAPI, con el back-end en ejecución:

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

Proyecto desarrollado para las materias Programación III (back-end) y Programación IV (front-end) – UTN Facultad Regional Mar del Plata.
