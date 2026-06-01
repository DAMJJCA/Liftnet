# Deploy a produccion

## Backend

Preparacion recomendada:

- Usar perfil `prod`: `SPRING_PROFILES_ACTIVE=prod`.
- Configurar variables de entorno reales en el hosting, no en archivos versionados.
- Activar seguridad JWT real en `SecurityConfig`.
- Revisar que `@EnableMethodSecurity` este activo si se usan `@PreAuthorize`.
- Verificar que CORS permita solo dominios reales del frontend.
- Ejecutar build Maven y pruebas antes de crear imagen.

Build local:

```bash
cd liftnet-backend
./mvnw clean package
```

Docker:

```bash
cd liftnet-backend
docker compose up --build
```

`docker-compose.yml` lee `liftnet-backend/.env` y permite definir `SPRING_PROFILES_ACTIVE`. Si no se define, el compose usa `prod` como fallback.

## Frontend web

Preparacion recomendada:

- Configurar `apiUrl` de produccion sin secretos.
- Revisar configuracion de Supabase en entorno de build.
- Ejecutar build:

```bash
cd liftnet-frontend
npm install
npm run build
```

Pendiente de confirmar:

- Hosting objetivo del frontend.
- Estrategia de variables de entorno para Angular.

## App movil

Preparacion recomendada:

- Sustituir `ApiConstants.baseUrl` por la URL publica de la API.
- Configurar Supabase sin hardcodear secretos privados.
- Revisar almacenamiento seguro de token.

Pendiente de confirmar:

- Android, iOS o ambos.
- Pipeline de build y firma.

## Variables de entorno necesarias

Backend:

- `SPRING_PROFILES_ACTIVE`
- `SERVER_PORT`
- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION`

Frontend/app:

- URL publica de API.
- URL de Supabase.
- Clave publica anon de Supabase si se decide usarla desde cliente.

No incluir valores reales en Git.

## Configuracion local del backend

Para Supabase directo confirmado:

- Host: `db.dixynkwbhduwjerffqsg.supabase.co`
- Puerto: `5432`
- Base de datos: `postgres`
- Usuario: `postgres`
- SSL: `require`

Crear un archivo local `liftnet-backend/.env` a partir de `liftnet-backend/.env.example`. Ese archivo no debe versionarse.

En esta maquina, la configuracion que arranco correctamente usa el pooler de Supabase:

```env
DB_URL=jdbc:postgresql://aws-0-eu-west-1.pooler.supabase.com:6543/postgres?sslmode=require
DB_USER=postgres.dixynkwbhduwjerffqsg
```

Contenido esperado, usando placeholders para secretos:

```env
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080
DB_URL=jdbc:postgresql://aws-0-eu-west-1.pooler.supabase.com:6543/postgres?sslmode=require
DB_USER=postgres.dixynkwbhduwjerffqsg
DB_PASSWORD=REPLACE_WITH_SUPABASE_DATABASE_PASSWORD
JWT_SECRET=REPLACE_WITH_STRONG_LOCAL_JWT_SECRET_MIN_32_CHARS
JWT_EXPIRATION=3600000
```

`application.yml` importa de forma opcional `liftnet-backend/.env`, asi que `mvnw spring-boot:run` puede arrancar leyendo ese archivo local cuando el comando se ejecuta desde `liftnet-backend/`.

Alternativamente, para arrancar con Maven en PowerShell sin depender del archivo `.env`, exportar las variables en la sesion antes de ejecutar:

```powershell
$env:SPRING_PROFILES_ACTIVE="dev"
$env:SERVER_PORT="8080"
$env:DB_URL="jdbc:postgresql://aws-0-eu-west-1.pooler.supabase.com:6543/postgres?sslmode=require"
$env:DB_USER="postgres.dixynkwbhduwjerffqsg"
$env:DB_PASSWORD="REPLACE_WITH_SUPABASE_DATABASE_PASSWORD"
$env:JWT_SECRET="REPLACE_WITH_STRONG_LOCAL_JWT_SECRET_MIN_32_CHARS"
$env:JWT_EXPIRATION="3600000"
.\mvnw.cmd spring-boot:run
```

No usar `postgres.dixynkwbhduwjerffqsg` como `DB_USER` para la conexion directa confirmada; el usuario directo confirmado es `postgres`. Para el pooler, `postgres.dixynkwbhduwjerffqsg` si es el usuario correcto.

Nota de conectividad: en la verificacion local, `db.dixynkwbhduwjerffqsg.supabase.co` resolvio como registro IPv6 (`AAAA`) y no se pudo abrir conexion TCP al puerto `5432` desde este entorno. Si la maquina o red local no tiene IPv6 funcional, la conexion directa puede fallar aunque las variables sean correctas. En ese caso, usar el endpoint pooler oficial de Supabase mostrado en el dashboard y ajustar `DB_URL`/`DB_USER` segun ese modo de conexion, sin inventar credenciales.

`JWT_SECRET` debe tener al menos 256 bits efectivos para HS256. En la practica, usar un valor aleatorio largo, minimo 32 caracteres ASCII, y no reutilizar secretos antiguos ni placeholders cortos.

## HikariCP para Supabase pooler

En `application-dev.yml` y en produccion es importante configurar HikariCP para evitar que el backend quede colgado por conexiones TCP obsoletas con el pooler de Supabase:

```yaml
spring:
  datasource:
    hikari:
      keepalive-time: 30000
      max-lifetime: 600000
      minimum-idle: 1
      maximum-pool-size: 5
      connection-timeout: 8000
      validation-timeout: 3000
      connection-test-query: SELECT 1
```

Sin esta configuracion, cuando Supabase free tier hiberna o el pooler cierra conexiones inactivas, HikariCP intenta usar conexiones TCP muertas. El resultado es que las peticiones HTTP al backend quedan colgadas indefinidamente (no fallan, no responden), causando pantallas de carga infinitas en el frontend.

El backend debe reiniciarse para que cambios en esta configuracion sean efectivos.

## Supabase/PostgreSQL

Recomendaciones:

- Usar SSL en conexion PostgreSQL.
- Rotar credenciales detectadas en archivos versionados.
- Configurar backups automaticos.
- Revisar politicas de Storage y exposicion publica de buckets.
- Documentar buckets reales y permisos.

## Checklist antes de desplegar

- No hay secretos reales en archivos versionados.
- `.env` y `*.env` estan ignorados.
- `application-dev.yml` no contiene defaults sensibles.
- Seguridad backend no esta en modo `permitAll`.
- `JWT_SECRET` es fuerte y esta fuera de Git.
- CORS restringido a dominios reales.
- Migraciones de base de datos existen y se han probado.
- Build backend correcto.
- Build frontend correcto.
- Logs no imprimen passwords ni tokens.
- Swagger esta restringido o conscientemente publicado.

## Que falta por decidir

- Hosting backend.
- Hosting frontend.
- Dominio final.
- Estrategia de CI/CD.
- Gestion de secretos.
- Politica de backups y restauracion.
- Activacion definitiva de JWT y roles en backend.
