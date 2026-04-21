LIFTNET BACKEND

Backend REST para Liftnet, una plataforma orientada al sector fitness profesional.
Este proyecto implementa autenticación segura con JWT, control de roles, reglas de negocio,
paginación, manejo global de errores y protección básica para producción.

El backend está preparado para ser consumido por un frontend SPA (Angular / React)
y para ser desplegado en entornos productivos.


					ESTADO DEL PROYECTO
============================================================

- Backend funcional
- Frontend-ready
- Seguridad básica de producción
- Manejo global y seguro de errores
- Timeouts configurados
- Variables de entorno dev / prod
- Rate limiting básico (MVP)
- Docker pendiente (limitación del equipo de trabajo)


					STACK TECNOLÓGICO
============================================================

- Java 17
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- Spring Data JPA
- PostgreSQL (Supabase)
- Flyway (migraciones de base de datos)
- Swagger / OpenAPI
- Maven
- SLF4J (logging)


					ARQUITECTURA GENERAL
============================================================

- Controllers: exponen endpoints REST
- Services: contienen la lógica de negocio
- Repositories: acceso a datos
- DTOs: objetos de entrada y salida
- Mappers: transformación entre entidades y DTOs
- Filtros: seguridad y rate limiting
- Config: seguridad, CORS, OpenAPI
- Manejo global de excepciones

El backend no expone entidades directamente.


					VERSIONADO DE LA API
============================================================

Toda la API está versionada bajo:

/api/v1/**

Esto permite evolucionar la API sin romper clientes existentes.


						SEGURIDAD
============================================================

AUTENTICACIÓN
- Basada en JWT
- Header usado:
  Authorization: Bearer <token>

AUTORIZACIÓN
Roles del sistema:
- POSTULANTE
- EMPRESA
- ADMIN

Autorización aplicada mediante @PreAuthorize.


					SEGURIDAD EXTRA (HARDENING)
============================================================

Configuraciones aplicadas:
- CSRF desactivado (correcto para JWT)
- Headers HTTP de seguridad:
  - X-Content-Type-Options
  - X-Frame-Options
  - Strict-Transport-Security
- Swagger permitido solo en rutas específicas
- Control de acceso estricto a endpoints


					RATE LIMITING (MVP)
============================================================

- Limitación por IP
- 100 peticiones por IP por minuto
- Implementación en memoria
- Respuesta HTTP 429 en caso de abuso

Ejemplo de respuesta:

{
  "success": false,
  "message": "Demasiadas peticiones. Intenta más tarde.",
  "data": null,
  "timestamp": "..."
}

Limitación conocida:
- No soporta escalado horizontal (suficiente para MVP)


					RESPUESTAS UNIFICADAS
============================================================

Toda la API responde usando el formato:

{
  "success": true | false,
  "message": "...",
  "data": {},
  "timestamp": "..."
}

Esto permite usar un único interceptor de errores en el frontend.


				MANEJO GLOBAL DE ERRORES
============================================================

- Implementado mediante GlobalExceptionHandler
- No se exponen stacktraces al frontend
- Logs completos en backend
- Mensajes controlados
- Respuestas siempre en formato ApiResponse

Errores gestionados:
- Validaciones (@Valid)
- Recursos no encontrados
- Errores de autenticación
- Reglas de negocio
- Errores internos no controlados


					TIMEOUTS Y ESTABILIDAD
============================================================

HTTP (Tomcat):
- Timeout de conexión
- Control de hilos
- Control de conexiones simultáneas

Base de datos:
- Pool de conexiones seguro
- Protección frente a bloqueos en PostgreSQL / Supabase


					VARIABLES DE ENTORNO
============================================================

El proyecto no contiene secretos hardcodeados.

Variables utilizadas:

SPRING_PROFILES_ACTIVE
SERVER_PORT

DB_URL
DB_USER
DB_PASSWORD

JWT_SECRET
JWT_EXPIRATION


					CONFIGURACIÓN DE ENTORNOS
============================================================

application.yml
- Configuración base común
- Define perfil activo

application-dev.yml
- Valores por defecto
- SQL visible
- Desarrollo local cómodo

application-prod.yml
- Variables obligatorias
- Sin valores hardcodeados
- Hibernate en modo validate
- SQL desactivado


					ENDPOINTS PRINCIPALES
============================================================

AUTENTICACIÓN
POST /api/v1/auth/login
POST /api/v1/auth/register

OFERTAS
GET  /api/v1/ofertas
POST /api/v1/ofertas
GET  /api/v1/ofertas/mis-ofertas
PUT  /api/v1/ofertas/{id}/cerrar

POSTULACIONES
POST /api/v1/postulaciones/oferta/{id}
GET  /api/v1/postulaciones/mis-postulaciones
GET  /api/v1/postulaciones/oferta/{id}
PUT  /api/v1/postulaciones/{id}/estado

ADMINISTRACIÓN
GET /api/v1/admin/usuarios
PUT /api/v1/admin/usuarios/{id}/rol
PUT /api/v1/admin/usuarios/{id}/enabled


						SWAGGER
============================================================

Documentación automática disponible en:

http://localhost:8080/swagger-ui.html

Incluye:
- Todos los endpoints
- DTOs
- Parámetros de paginación
- Autenticación JWT (Authorize)


						DOCKER
============================================================

PENDIENTE

El proyecto está preparado para Docker, pero no se ha ejecutado aún
debido a limitaciones del equipo de trabajo.

Se retomará en un entorno personal.

Archivos planificados:
- Dockerfile
- docker-compose.yml
- .env

						AUTOR
============================================================

Proyecto desarrollado como backend base para la plataforma Liftnet 
y elaborado por Jorge Jose Cristancho Avila.