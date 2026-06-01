# Base de datos

## Motor detectado

Motor detectado: PostgreSQL en Supabase.

El esquema real fue aportado como contexto del proyecto. No incluir aqui URLs, usuarios, passwords ni claves reales.

## Entidades/tablas detectadas

- `users`: usuarios del sistema.
- `postulante_profile`: perfil de postulante.
- `empresa_profile`: perfil de empresa.
- `oferta_trabajo`: ofertas publicadas por empresas.
- `postulacion`: postulaciones de postulantes a ofertas.
- `experiencia`: experiencias asociadas a postulantes.
- `certificacion`: catalogo de certificaciones.
- `postulante_certificacion`: relacion entre postulantes y certificaciones.

Enum detectado:

- `Role`: `POSTULANTE`, `EMPRESA`, `ADMIN`.
- `EstadoPostulacion`: `PENDIENTE`, `ACEPTADA`, `RECHAZADA`.

## Esquema Supabase confirmado

Tablas publicas confirmadas:

- `users`: `id`, `email`, `password`, `role`, `enabled`, `created_at`, `updated_at`, `deleted_at`.
- `postulante_profile`: `id`, `user_id`, `nombre`, `apellidos`, `ubicacion`, `bio`, `telefono`, `disponible`, `created_at`, `updated_at`, `deleted_at`, `foto_url`, `cv_url`.
- `empresa_profile`: `id`, `user_id`, `nombre_empresa`, `ubicacion`, `descripcion`, `telefono`, `created_at`, `updated_at`, `deleted_at`, `foto_url`.
- `oferta_trabajo`: `id`, `empresa_id`, `titulo`, `descripcion`, `ubicacion`, `fecha_inicio`, `fecha_fin`, `activa`, `created_at`, `updated_at`, `deleted_at`, `estudios_minimos`, `experiencia_minima`, `idiomas`, `nivel`, `vacantes`, `salario`.
- `postulacion`: `id`, `oferta_id`, `postulante_id`, `estado`, `created_at`, `updated_at`.
- `experiencia`: `id`, `postulante_id`, `lugar`, `descripcion`, `fecha_inicio`, `fecha_fin`, `created_at`, `updated_at`, `deleted_at`, `archivo_url`.
- `certificacion`: `id`, `nombre`, `entidad`, `created_at`, `updated_at`, `deleted_at`.
- `postulante_certificacion`: `id`, `postulante_id`, `certificacion_id`, `fecha_obtencion`, `fecha_expiracion`, `created_at`, `archivo_url`.

Todas las tablas usan `uuid` con `gen_random_uuid()` como default en base de datos.

El campo `postulacion.estado` usa el tipo PostgreSQL `estado_postulacion` con default `PENDIENTE`.

## Relaciones principales

- `users` 1:1 `postulante_profile`.
- `users` 1:1 `empresa_profile`.
- `empresa_profile` 1:N `oferta_trabajo`.
- `oferta_trabajo` 1:N `postulacion`.
- `postulante_profile` 1:N `postulacion`.
- `postulante_profile` 1:N `experiencia`.
- `postulante_profile` 1:N `postulante_certificacion`.
- `certificacion` 1:N `postulante_certificacion`.

Restricciones confirmadas en Supabase:

- `users.email` es unico.
- `postulante_profile.user_id` es unico y referencia `users.id`.
- `empresa_profile.user_id` es unico y referencia `users.id`.
- `oferta_trabajo.empresa_id` referencia `empresa_profile.id`.
- `postulacion.oferta_id` referencia `oferta_trabajo.id`.
- `postulacion.postulante_id` referencia `postulante_profile.id`.
- `experiencia.postulante_id` referencia `postulante_profile.id`.
- `postulante_certificacion.postulante_id` referencia `postulante_profile.id`.
- `postulante_certificacion.certificacion_id` referencia `certificacion.id`.

Nota: el codigo JPA declara una restriccion unica sobre `postulacion(oferta_id, postulante_id)`, pero el esquema Supabase aportado no la muestra. Esto debe revisarse antes de depender de la base para evitar postulaciones duplicadas.

## Migraciones

Flyway esta configurado en `application-dev.yml` y `application-prod.yml`:

- `spring.flyway.enabled: true`
- `spring.flyway.locations: classpath:db/migration`

No se encontraron archivos de migracion en `liftnet-backend/src/main/resources/db/migration`.

El esquema Supabase existe fuera de Flyway actualmente. Para ordenar el proyecto, conviene crear una migracion base que represente el estado actual o adoptar una estrategia clara de baseline.

## Hibernate

Configuracion detectada:

- `ddl-auto: validate` en dev y prod.
- `show-sql: true` en dev.
- `show-sql: false` en prod.

Con `ddl-auto: validate`, la base debe existir y coincidir con las entidades antes de arrancar.

## Configuracion necesaria sin secretos

Variables esperadas:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `SPRING_PROFILES_ACTIVE`
- `SERVER_PORT`

No versionar valores reales.

## Pendientes o inconsistencias detectadas

- Flyway esta activo pero faltan migraciones versionadas.
- Existen entidades con campos `createdAt`, `updatedAt` o `deletedAt` sin estrategia uniforme visible.
- `Postulacion.estado` usa tipo PostgreSQL enum `estado_postulacion`; el tipo existe en Supabase, pero la migracion que lo crea no esta en el repo.
- `postulacion(oferta_id, postulante_id)` es unica en JPA, pero no aparece como unique constraint en el esquema aportado.
- `oferta_trabajo` tiene `fecha_inicio` y `fecha_fin` en Supabase, pero esos campos no aparecen en la entidad `OfertaTrabajo` revisada.
- `postulante_profile.apellidos` aparece nullable en Supabase, mientras el codigo lo marca como no nulo.
- `experiencia.lugar` aparece nullable en Supabase, mientras el codigo lo marca como no nulo.
- Pendiente de confirmar si `certificacion` se carga por seed/migracion o manualmente.
