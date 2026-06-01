# Backups

## Estrategia recomendada

La estrategia debe cubrir base de datos, configuracion no secreta, configuracion secreta fuera de Git y archivos subidos a Supabase Storage.

## Que se debe guardar

- Dump de PostgreSQL/Supabase.
- Metadatos de esquema y migraciones Flyway cuando existan.
- Configuracion de despliegue sin secretos.
- Variables de entorno en gestor seguro de secretos.
- Buckets de Supabase Storage usados por CVs, fotos, experiencias y certificaciones.
- Version exacta del codigo desplegado.

## Que no se debe versionar

- `.env`
- `*.env`
- Passwords de base de datos.
- `JWT_SECRET`.
- Claves Supabase.
- Dumps completos de base de datos con datos personales.
- Archivos subidos por usuarios si contienen informacion personal.
- `application-local.properties`, `application-local.yml`, `application-local.yaml`.

## Backup de base de datos

Recomendado para PostgreSQL/Supabase:

- Backup diario automatico en el proveedor si esta disponible.
- Export periodico con `pg_dump` para entornos criticos.
- Retencion minima recomendada: 7 dias diarios, 4 semanas semanales y 3 meses mensuales para produccion inicial.

Pendiente de confirmar:

- Plan Supabase contratado y capacidades de backup/restauracion.
- Tamano esperado de datos y RPO/RTO.

## Backup de configuracion

- Mantener plantillas `.env.example` sin secretos.
- Guardar secretos reales en Supabase, plataforma de hosting o gestor de secretos.
- Documentar variables requeridas en `docs/deploy-produccion.md`.

## Restauracion basica

1. Crear o seleccionar instancia PostgreSQL/Supabase.
2. Restaurar dump de base de datos.
3. Restaurar buckets/archivos si aplica.
4. Configurar variables de entorno reales en el entorno, no en Git.
5. Arrancar backend con perfil `prod`.
6. Verificar `/actuator/health` si esta expuesto y pruebas basicas de login/ofertas.

## Frecuencia recomendada

- Desarrollo: backup manual antes de cambios grandes de esquema.
- Preproduccion: diario si contiene datos utiles.
- Produccion: diario como minimo; aumentar frecuencia si hay usuarios reales activos.

