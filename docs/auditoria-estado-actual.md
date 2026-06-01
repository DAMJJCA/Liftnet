# Auditoria de estado actual

Fecha: 2026-05-31

## Objetivo de producto

Liftnet apunta a ser una plataforma tipo LinkedIn/InfoJobs especializada en socorristas y empresas que buscan socorristas. La prioridad actual es lanzar una version web usable centrada en `POSTULANTE` y `EMPRESA`. `ADMIN` debe mantenerse, pero no debe bloquear el MVP. Flutter queda para una fase posterior, reutilizando la misma API.

## Backend Spring Boot

### Modulos implementados

Implementado:

- `auth`: login, registro, JWT y respuesta de autenticacion.
- `user`: usuario, roles y gestion basica de usuarios para admin.
- `postulante`: perfil de postulante.
- `empresa`: perfil de empresa.
- `oferta`: ofertas de trabajo.
- `postulacion`: postulaciones y estados.
- `experiencia`: experiencias de postulante.
- `certificacion`: catalogo/asignacion de certificaciones a postulante.
- `common`: `ApiResponse`, excepciones, mappers.
- `config`: seguridad, CORS, OpenAPI, JPA, rate limiting.

Parcial:

- Seguridad JWT: existen `JwtService`, `JwtAuthenticationFilter` y tokens, pero `SecurityConfig` activo esta en modo desarrollo con `permitAll`.
- Identidad: muchos endpoints usan `email` como query param inyectado por Angular; parte de empresa usa `SecurityContext`, lo que crea una mezcla de enfoques.
- Flyway: esta configurado, pero no hay migraciones versionadas.

### Controladores existentes

- `AuthController`: `POST /api/v1/auth/login`, `POST /api/v1/auth/register`.
- `UserController`: `GET /api/v1/users` como endpoint dev/test.
- `AdminController`: `GET /api/v1/admin/usuarios`, `PUT /api/v1/admin/usuarios/{id}/rol`, `PUT /api/v1/admin/usuarios/{id}/enabled`.
- `PostulanteController`: perfil postulante, crear/actualizar perfil, disponibilidad.
- `EmpresaController`: perfil empresa, crear/actualizar perfil.
- `OfertaController`: listar ofertas activas, crear, editar, listar propias, cerrar.
- `PostulacionController`: postularse, mis postulaciones, postulaciones de oferta, cambiar estado.
- `ExperienciaController`: listar/crear/actualizar/eliminar experiencias.
- `CertificacionController`: listar/asignar/eliminar certificaciones del postulante.

### Servicios existentes

- `AuthService`: registro/login, hashing de password, generacion JWT, calculo de perfil completado.
- `UserService`: listar usuarios, cambiar rol, activar/desactivar.
- `PostulanteService`: perfil, actualizacion, disponibilidad, creacion vacia preparada.
- `EmpresaService`: perfil empresa desde `SecurityContext`, creacion vacia preparada.
- `OfertaService`: crear, editar, listar activas, listar propias, cerrar, validacion de propiedad.
- `PostulacionService`: crear postulacion, listar postulaciones, listar candidatos de una oferta, cambiar estado, evitar duplicados por repositorio y devolver `409 Conflict` en duplicado.
- `ExperienciaService`: CRUD de experiencias con validacion de propiedad por postulante.
- `CertificacionService`: asignar/listar/eliminar certificaciones con validacion de propiedad.
- `JwtService`, `RateLimitingFilter`, `PasswordEncoderConfig`.

### DTOs existentes

- Auth: `AuthRequest`, `RegisterRequest`, `AuthResponse`.
- Usuario/admin: `UserResponse`.
- Postulante: `PostulanteProfileRequest`, `PostulanteProfileResponse`.
- Empresa: `EmpresaProfileRequest`, `EmpresaProfileResponse`.
- Oferta: `OfertaRequest`, `OfertaResponse`.
- Postulacion: `PostulacionResponse`, `PostulantePostulacionResponse`.
- Experiencia: `ExperienciaRequest`, `ExperienciaResponse`.
- Certificacion: `AsignarCertificacionRequest`, `CertificacionCatalogoResponse`, `CertificacionPostulanteResponse`.
- Comun: `ApiResponse`, `ApiError`.

### Entidades existentes

- `User`
- `Role`: `POSTULANTE`, `EMPRESA`, `ADMIN`
- `PostulanteProfile`
- `EmpresaProfile`
- `OfertaTrabajo`
- `Postulacion`
- `EstadoPostulacion`: `PENDIENTE`, `ACEPTADA`, `RECHAZADA`
- `Experiencia`
- `Certificacion`
- `PostulanteCertificacion`

### Endpoints disponibles

Autenticacion:

- `POST /api/v1/auth/login`
- `POST /api/v1/auth/register`

Postulante:

- `GET /api/v1/postulante/profile`
- `POST /api/v1/postulante/profile`
- `PUT /api/v1/postulante/profile`
- `PATCH /api/v1/postulante/profile/disponibilidad`
- `GET /api/v1/experiencias/mis-experiencias`
- `POST /api/v1/experiencias`
- `PUT /api/v1/experiencias/{id}`
- `DELETE /api/v1/experiencias/{id}`
- `GET /api/v1/certificaciones/mis-certificaciones`
- `POST /api/v1/certificaciones/asignar`
- `DELETE /api/v1/certificaciones/{id}`

Empresa/ofertas:

- `GET /api/v1/empresa/profile`
- `POST /api/v1/empresa/profile`
- `PUT /api/v1/empresa/profile`
- `GET /api/v1/ofertas`
- `POST /api/v1/ofertas`
- `PUT /api/v1/ofertas/{ofertaId}`
- `GET /api/v1/ofertas/mis-ofertas`
- `PUT /api/v1/ofertas/{ofertaId}/cerrar`

Postulaciones:

- `POST /api/v1/postulaciones/oferta/{ofertaId}`
- `GET /api/v1/postulaciones/mis-postulaciones`
- `GET /api/v1/postulaciones/oferta/{ofertaId}`
- `PUT /api/v1/postulaciones/{postulacionId}/estado`

Admin:

- `GET /api/v1/admin/usuarios`
- `PUT /api/v1/admin/usuarios/{id}/rol`
- `PUT /api/v1/admin/usuarios/{id}/enabled`

### Completo, parcial o roto

Implementado y reutilizable:

- Login/registro con JWT.
- Perfiles de postulante/empresa.
- Ofertas: crear, editar, listar, cerrar.
- Postulaciones: aplicar, listar propias, listar candidatos, aceptar/rechazar, retirar candidatura (DELETE si PENDIENTE).
- Experiencias y certificaciones.
- Admin basico de usuarios.

Parcial:

- Seguridad real en backend.
- Contrato de respuesta uniforme: algunos controladores devuelven `ApiResponse`, otros DTO/`ResponseEntity` directo.
- `EmpresaController` usa `SecurityContext`, pero la seguridad activa esta en `permitAll`; depende de que el filtro JWT este realmente conectado para produccion.
- Admin Angular espera arrays simples, pero backend devuelve `Page`; puede requerir ajuste.
- Entidades JPA no estan completamente alineadas con esquema Supabase documentado.
- Flutter consume formatos distintos a los `ApiResponse<Page<...>>` del backend.

Corregido (2026-06-01):

- Error intermitente "/ofertas no carga o queda en loading": causa raiz era la combinacion de HikariCP usando conexiones TCP obsoletas con Supabase pooler (la peticion HTTP quedaba colgada indefinidamente, sin error, sin respuesta) y el operador `takeUntilDestroyed` cancelando la peticion al navegar a otra ruta, impidiendo que calentara la conexion para la siguiente visita. Solucion final: HikariCP configurado con `validation-timeout: 3000`, `connection-test-query: SELECT 1`, `connection-timeout: 8000` y `keepalive-time: 30000`; en Angular: `timeout(8000)` + `retry({ count: 2, delay: 800 })` en `getOfertasActivas()` sin `takeUntilDestroyed`; `getMisPostulaciones()` si usa `takeUntilDestroyed`. Estado local migrado a Angular Signals para eliminar el hack de referencia forzada `new Map(this.postuladas)`.

Roto o riesgo alto:

- `AuthService.register` no crea perfil vacio aunque existen metodos `createEmptyProfile`; despues el frontend intenta crear perfil manualmente. Funciona como flujo si no se depende del perfil vacio, pero hay codigo no usado.
- `AuthService.setRole` existe en Angular, pero no se encontro endpoint backend `/auth/set-role`.
- `EmpresaProfileComponent.saveProfile()` ya navega a `/empresa/ofertas/mis-ofertas`. Corregido tras la auditoria inicial.
- `PostulanteProfileStore` decide crear vs actualizar segun `profileCompleted` guardado en localStorage; si hay perfil en BD pero localStorage dice false, podria intentar crear duplicado. **CORREGIDO (2026-05-31): ahora usa `profileExistsInDB` signal, actualizado desde la respuesta real del backend en `cargarTodo()`, en vez de localStorage.**

### Codigo reutilizable para futuras funciones

- `PostulacionService` y `PostulacionRepository`: base natural para chat empresa-postulante.
- `PostulantePostulacionResponse`: ya agrupa candidato, experiencias, certificaciones y CV para empresa.
- `EmpresaOfertasStore`: patron reutilizable para estado de ofertas.
- `PostulanteProfileStore`: patron reutilizable para cargar varias fuentes en paralelo.
- `TokenStorageService` + `authInterceptor`: capa actual de sesion web.
- `ApiResponse` y `GlobalExceptionHandler`: base para estandarizar respuestas.

## Frontend Angular

### Rutas existentes

Publicas:

- `/`
- `/login`
- `/register`

Postulante:

- `/ofertas`
- `/postulante/perfil`
- `/postulante/postulaciones`
- `/postulante/experiencias`
- `/postulante/certificaciones`

Empresa:

- `/empresa/perfil`
- `/empresa/ofertas/lista`
- `/empresa/ofertas/mis-ofertas`
- `/empresa/ofertas/:ofertaId/postulaciones`

Admin:

- `/admin`

### Paginas/componentes detectados

Postulante:

- `PostulanteProfileComponent`
- `PostulacionesComponent`
- `MisExperienciasComponent`
- `MisCertificacionesComponent`

Empresa:

- `EmpresaProfileComponent`
- `ListaOfertasComponent`
- `MisOfertasComponent`
- `EmpresaPostulacionesComponent`

Admin:

- `AdminComponent`

Compartidos/base:

- `NavbarComponent`
- `HomeComponent`
- servicios core, guards, interceptores, stores.

Hay carpeta `documentosHanin` con componentes/documentos paralelos. Pendiente de confirmar si son documentacion visual, prototipos o codigo muerto.

### Servicios que llaman al backend

- `AuthService`
- `PostulanteService`
- `EmpresaService`
- `OfertaService`
- `PostulacionService`
- `ExperienciasService`
- `CertificacionesService`
- `AdminService`
- `SupabaseService` para Storage, no backend.

### Flujo funcionando

Implementado:

- Login/registro guarda token, email, rol y estado de perfil.
- Interceptor agrega `Authorization` y `email` query param para modo desarrollo.
- Guards por autenticacion, rol y perfil.
- Perfil postulante con foto/CV via Supabase Storage.
- Perfil empresa con logo via Supabase Storage.
- Empresa puede gestionar ofertas.
- Postulante puede ver ofertas activas en `/ofertas` aunque el perfil no esté completo. `ListaOfertasComponent` muestra estado de carga antes de renderizar resultados o estado vacío. Si intenta postularse sin perfil completo, aparece un modal con acceso directo a completar perfil.
- Empresa puede ver postulantes por oferta y cambiar estado.
- Postulante puede ver sus postulaciones.

Parcial:

- Validaciones de formularios son basicas.
- Estados de carga/error no estan homogeneizados.
- Algunos servicios tipan `any` por diferencias de formato de respuesta.
- Admin parece basico y puede fallar si no adapta `Page` a array.
- UX de navegacion tras crear perfil empresa apunta a una ruta padre incompleta.

### Componentes reutilizables

- `NavbarComponent` para navegacion por rol.
- Stores basados en Angular signals (`EmpresaOfertasStore`, `PostulanteProfileStore`).
- `SupabaseService` para uploads.
- `TokenStorageService` y guards.

## App Flutter

### Implementado

- Estructura Flutter con Riverpod, GoRouter, Dio y Flutter Secure Storage.
- Pantallas: login, registro, feed, detalle de oferta, perfil, mis ofertas, crear oferta.
- Repositorios iniciales para auth, ofertas, empresa y perfil.
- Interceptor que agrega `Authorization: Bearer <token>`.
- Supabase Flutter inicializado.

### Parcial o pendiente

- Rutas y repositorios no parecen alineados del todo con backend actual.
- `OfertasRepositoryImpl` espera lista directa, pero backend devuelve `ApiResponse` con `Page`.
- `ProfileRepository` usa rutas `/postulantes/...` que no existen en backend actual (`/postulante/profile`).
- Pantalla de postulaciones en router es placeholder.
- No hay flujo completo empresa-postulante en Flutter.

### Decisiones actuales que afectan a Flutter

- Conviene estabilizar contrato REST antes de ampliar app movil.
- Evitar endpoints pensados solo para Angular; usar DTOs comunes y paginacion consistente.
- Sustituir `email` query param por identidad de token beneficiara a Flutter.
- Chat deberia nacer como API REST estable antes de WebSockets para que Flutter pueda consumirlo tambien.

## Riesgos tecnicos principales

- Seguridad backend en `permitAll`.
- Mezcla de identidad por `email` query param y `SecurityContext`.
- Desalineacion JPA/Supabase.
- Falta de migraciones Flyway.
- Respuestas no uniformes (`ApiResponse`, DTO directo, `Page`).
- Secretos historicos/versionados ya documentados en seguridad.
- Futura app movil puede romperse si no se estabiliza contrato API.
