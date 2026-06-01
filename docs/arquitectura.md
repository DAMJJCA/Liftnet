# Arquitectura

## Resumen general

Liftnet esta organizado como un sistema con tres clientes/componentes principales:

- Backend REST en Spring Boot.
- Frontend web en Angular.
- App movil en Flutter.

El dominio principal cubre usuarios, roles, perfiles de postulante, perfiles de empresa, ofertas, postulaciones, experiencias y certificaciones.

## Backend

El backend esta en `liftnet-backend/` y expone una API bajo `/api/v1/**`.

Capas detectadas:

- `controller`: endpoints REST.
- `service`: logica de negocio.
- `repository`: acceso a PostgreSQL con Spring Data JPA.
- `entity`: entidades JPA.
- `dto`: objetos de entrada/salida.
- `common`: respuestas, excepciones y mappers.
- `config`: seguridad, CORS, OpenAPI, persistencia y rate limiting.

Dependencias principales: Spring Web, Spring Security, Spring Data JPA, Validation, PostgreSQL driver, JJWT y Springdoc OpenAPI.

## Frontend web

El frontend web esta en `liftnet-frontend/` y usa Angular.

Elementos detectados:

- Rutas publicas: home, login, register.
- Rutas de `POSTULANTE`: perfil, postulaciones, experiencias, certificaciones, `/ofertas`.
- Rutas de `EMPRESA`: perfil, lista de ofertas, mis ofertas, postulaciones de una oferta.
- Ruta de `ADMIN`: panel admin.
- Guards: `authGuard`, `roleGuard`, `profileGuard`. La ruta `/ofertas` no usa `profileGuard`; el postulante puede ver ofertas aunque el perfil no este completo.
- Interceptor HTTP: agrega `Authorization: Bearer <token>` y, en modo desarrollo, agrega `email` como query param a peticiones no auth.
- Servicios de API por dominio y servicio Supabase para subida/borrado de archivos.
- Estado local en componentes criticos usa Angular Signals (`signal`, `takeUntilDestroyed`). Ejemplo: `ListaOfertasComponent` usa signals para `loading`, `error`, `ofertas`, `postuladas` y `mensaje`, garantizando actualizacion reactiva sin depender de zone.js.

## App movil

La app movil esta en `liftnet_frontend_app/` y usa Flutter.

Elementos detectados:

- Router con GoRouter.
- Estado con Riverpod.
- HTTP con Dio.
- Token en `FlutterSecureStorage`.
- Interceptor que agrega `Authorization: Bearer <token>`.
- Supabase Flutter inicializado desde constantes.
- Features detectadas: auth, feed, profile, company offers, postulations.

## Flujo general

1. Usuario entra desde Angular o Flutter.
2. Login/registro llama a `POST /api/v1/auth/login` o `POST /api/v1/auth/register`.
3. Backend valida credenciales, genera JWT y devuelve rol y estado de perfil.
4. El cliente guarda token y rol.
5. El cliente llama endpoints de perfiles, ofertas, postulaciones, experiencias o certificaciones.
6. Backend consulta o persiste datos mediante JPA en PostgreSQL/Supabase.
7. Para archivos, el frontend web usa Supabase Storage y guarda URLs en campos como `fotoUrl`, `cvUrl` o `archivoUrl`.

## Autenticacion JWT

Existe `JwtService`, `JwtAuthenticationFilter` y generacion de JWT en `AuthService`.

Estado real detectado:

- Login y registro generan JWT.
- El frontend web y la app movil envian `Authorization: Bearer <token>` cuando hay token.
- `SecurityConfig` tiene activo un modo desarrollo que permite todas las peticiones con `.anyRequest().permitAll()`.
- La configuracion de produccion con filtro JWT, rate limiting, headers y autenticacion esta comentada.

Conclusion: JWT existe, pero la proteccion global de endpoints no esta activa en la configuracion actual.

## Comunicacion entre partes

- Angular usa `environment.development.ts` con `apiUrl` local y Supabase.
- Flutter usa `ApiConstants.baseUrl` apuntando a `10.0.2.2:8080/api/v1`.
- Backend acepta CORS para `http://localhost:4200`.
- Backend responde en algunos controladores con `ApiResponse`; otros devuelven DTOs o `ResponseEntity` directamente.
- El esquema Supabase confirmado contiene tablas para usuarios, perfiles, ofertas, postulaciones, experiencias y certificaciones. Ver `docs/base-datos.md`.

## Configuracion de conexion notable

HikariCP esta configurado en `application-dev.yml` con parametros especificos para el pooler de Supabase:

- `keepalive-time: 30000`: mantiene conexiones activas con pings cada 30 segundos.
- `validation-timeout: 3000`: detecta conexiones muertas en 3 segundos.
- `connection-test-query: SELECT 1`: evita prepared statements del JDBC `isValid()`.
- `connection-timeout: 8000`: falla rapido si no hay conexion disponible en 8 segundos.

Esto es critico para la estabilidad en desarrollo porque Supabase free tier puede hibernar y dejar conexiones obsoletas en el pool.

## Pendientes de confirmar

- Dominio/URL final de produccion.
- Estrategia definitiva de Supabase Storage y permisos de buckets.
- Si app movil debe cubrir toda la funcionalidad del frontend web.
- Si se activara JWT en backend antes de seguir ampliando funcionalidades.
