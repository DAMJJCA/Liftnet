# Seguridad

## Riesgos actuales encontrados

- El `SecurityConfig` activo permite todas las peticiones con `anyRequest().permitAll()`.
- Varios endpoints de modo desarrollo identifican al usuario con `email` por query param.
- Hay configuracion JWT y `@PreAuthorize`, pero parte importante esta comentada o no efectiva por la configuracion activa.
- Existen secretos o credenciales aparentes en archivos versionados.
- Flyway esta configurado, pero no hay migraciones en el repo para auditar el esquema.
- El frontend web guarda JWT, rol y email en `localStorage`.
- CORS solo permite `http://localhost:4200`; falta configuracion final para produccion.

## Secretos detectados

No se copian valores por seguridad.

Se detectaron valores sensibles o potencialmente sensibles en:

- `liftnet-backend/.env`.
- `liftnet-backend/src/main/resources/application-dev.yml`: tenia defaults de conexion a base de datos y `JWT_SECRET`; se deben revisar commits/historial si ya se compartio.
- `liftnet-frontend/src/environments/environment.development.ts`: URL y clave Supabase.

Recomendacion: rotar credenciales de base de datos, `JWT_SECRET` y claves Supabase afectadas. Aunque una clave anon de Supabase puede ser publica por diseno, debe revisarse junto con las politicas RLS/Storage antes de darla por segura.

## Configuracion local segura

- Usar `liftnet-backend/.env.example` como plantilla.
- Crear `liftnet-backend/.env` solo en local.
- No versionar `.env`, `*.env`, `application-local.properties`, `application-local.yml` ni `application-local.yaml`.
- Para Supabase directo, usar `DB_USER=postgres` y `DB_URL` con host `db.dixynkwbhduwjerffqsg.supabase.co`, puerto `5432`, base `postgres` y `sslmode=require`.
- La password real de Supabase debe quedarse fuera del repositorio.
- `JWT_SECRET` debe ser suficientemente largo para HS256; valores cortos provocan `WeakKeyException` y no arrancan la aplicacion.
- Si la conexion directa a Supabase no funciona por IPv6/red, usar el pooler oficial indicado en el dashboard de Supabase y documentar el cambio sin guardar passwords.

## JWT

Detectado:

- `JwtService` genera tokens con `subject=email` y claim `role`.
- `JwtAuthenticationFilter` lee `Authorization: Bearer <token>`.
- El frontend web y la app movil envian el header cuando hay token.

Riesgo:

- La cadena de seguridad activa no usa el filtro JWT porque esta en modo desarrollo con `permitAll`.

Recomendacion:

- Activar configuracion de produccion con JWT.
- Confirmar expiracion.
- Usar `JWT_SECRET` fuerte y externo al repo.
- Manejar errores de token invalido/expirado.

## CORS

Detectado:

- CORS permite `http://localhost:4200`.
- Metodos permitidos: `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `OPTIONS`.
- Headers permitidos: `*`.
- Credenciales habilitadas.

Pendiente de confirmar:

- Dominio real de frontend.
- Si la app movil necesita configuracion CORS o solo consume API nativa.

## Validaciones

Detectado:

- Uso de `@Valid` en varios endpoints.
- Dependencia `spring-boot-starter-validation`.
- Manejo global de excepciones presente.

Pendiente de confirmar:

- Cobertura completa de constraints en DTOs.
- Validacion de ownership y permisos en todos los servicios.

## Roles y permisos

Detectado:

- Roles `POSTULANTE`, `EMPRESA`, `ADMIN`.
- Guards frontend por rol.
- `@PreAuthorize` activo en `EmpresaController`; en otros controladores hay bloques comentados para produccion.

Riesgo:

- Los guards frontend no sustituyen la autorizacion backend.
- Con `permitAll`, los endpoints quedan abiertos aunque tengan intencion de roles.

## Proteccion de endpoints

Estado actual:

- Login y registro publicos.
- Resto deberia protegerse segun rol, pero la configuracion activa permite todo.

Recomendacion:

- Proteger todos los endpoints no publicos.
- Eliminar `email` query param como mecanismo de identidad.
- Derivar usuario desde `Authentication`.
- Mantener checks de propiedad de recurso en servicios.

## Buenas practicas para `.env` y `.gitignore`

Se anadio proteccion en `.gitignore` para:

- `.env`
- `*.env`
- `application-local.properties`
- `application-local.yml`
- `application-local.yaml`

Recomendado:

- Crear `.env.example` sin valores reales.
- Eliminar secretos ya versionados en una tarea separada y rotarlos.
- Nunca subir dumps, claves privadas, tokens ni passwords.
- Revisar historial Git si el repo ya fue compartido.
