# AGENTS.md

## Objetivo del proyecto

Liftnet es una plataforma tipo LinkedIn/InfoJobs especializada en socorristas y empresas que buscan socorristas. El sistema conecta perfiles de postulantes con empresas que publican ofertas de trabajo. La prioridad actual es lanzar un MVP web usable para `POSTULANTE` y `EMPRESA`; `ADMIN` se mejora progresivamente y Flutter queda para una fase posterior reutilizando la misma API.

## Stack detectado

- Backend: Java 17, Spring Boot 3.3.5, Spring Security, Spring Data JPA, Bean Validation, PostgreSQL, JWT con `jjwt`, Springdoc OpenAPI, Maven, Docker.
- Frontend web: Angular 21, TypeScript, RxJS, Angular Router, Angular Forms, Angular Signals, Supabase JS.
- App movil: Flutter, Dart, Riverpod, GoRouter, Dio, Flutter Secure Storage, Supabase Flutter.
- Base de datos: PostgreSQL en Supabase. El esquema real fue aportado como contexto en la documentacion. Flyway esta configurado, pero no se encontraron migraciones en `src/main/resources/db/migration`.

## Estructura de carpetas

- `liftnet-backend/`: API REST Spring Boot.
- `liftnet-backend/src/main/java/com/liftnet/liftnet_backend/`: codigo Java por dominios (`auth`, `user`, `postulante`, `empresa`, `oferta`, `postulacion`, `experiencia`, `certificacion`, `config`, `common`).
- `liftnet-backend/src/main/resources/`: configuracion Spring (`application.yml`, `application-dev.yml`, `application-prod.yml`).
- `liftnet-frontend/`: frontend web Angular.
- `liftnet-frontend/src/app/`: componentes, rutas, guards, interceptores, servicios y stores.
- `liftnet-frontend/src/environments/`: configuracion de entornos Angular.
- `liftnet_frontend_app/`: app movil Flutter.
- `liftnet_frontend_app/lib/`: app, router, networking, features y constantes.
- `docs/`: documentacion base para trabajar con IA/Codex.

## Reglas para Codex

- Leer `AGENTS.md` y los documentos de `docs/` antes de modificar el proyecto.
- No modificar codigo funcional si el usuario pide solo documentacion, auditoria o plan.
- No inventar tablas, endpoints, roles, permisos ni funcionalidades.
- Si algo no esta claro en el codigo, escribir `Pendiente de confirmar`.
- Hacer cambios pequenos, revisables y localizados.
- Respetar la arquitectura existente por dominios en backend y features en frontend/app.
- No borrar archivos.
- No reescribir configuraciones de seguridad o despliegue sin confirmacion explicita.
- No copiar secretos reales en documentacion, mensajes, commits ni ejemplos.

## Como arrancar

### Backend

Desde `liftnet-backend/`:

```bash
./mvnw spring-boot:run
```

En Windows:

```bash
mvnw.cmd spring-boot:run
```

Perfil por defecto detectado: `dev`. Puerto por defecto: `8080`.

Docker existe:

```bash
docker compose up --build
```

Usa `liftnet-backend/.env`, que no debe versionarse.

### Frontend web

Desde `liftnet-frontend/`:

```bash
npm install
npm start
```

URL por defecto: `http://localhost:4200`.

### App movil

Desde `liftnet_frontend_app/`:

```bash
flutter pub get
flutter run
```

La API movil apunta a `http://10.0.2.2:8080/api/v1`, adecuado para emulador Android.

## Documentos que consultar antes de trabajar

- `docs/arquitectura.md`: vision tecnica general.
- `docs/arquitectura-roles.md`: roles y permisos detectados.
- `docs/base-datos.md`: entidades, relaciones y migraciones.
- `docs/seguridad.md`: riesgos, secretos y normas de seguridad.
- `docs/deploy-produccion.md`: despliegue y variables necesarias.
- `docs/backups.md`: copias de seguridad.
- `docs/roadmap.md`: estado y prioridades.
- `docs/auditoria-estado-actual.md`: auditoria tecnica del codigo real.
- `docs/mvp-web.md`: flujo MVP web y criterios de lanzamiento.
- `docs/chat.md`: analisis previo del chat empresa-postulante.

## Normas de seguridad

- No tocar secretos reales, tokens, passwords, `JWT_SECRET`, claves Supabase ni URLs privadas salvo para eliminarlos o sustituirlos por variables, y solo con instruccion clara.
- No imprimir secretos en logs, documentacion ni respuestas.
- Si se detecta un secreto versionado, documentarlo en `docs/seguridad.md` sin copiar su valor y recomendar rotacion.
- Usar variables de entorno para `DB_URL`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION`, `SERVER_PORT` y configuraciones equivalentes.
- Confirmar CORS, JWT, roles y proteccion de endpoints antes de preparar produccion.

## Normas para commits

- Commits pequenos, con un unico objetivo.
- Mensajes claros en imperativo o descriptivos, por ejemplo: `docs: add base project documentation`.
- No mezclar documentacion con cambios funcionales salvo que el usuario lo pida.
- Revisar `git status` antes de proponer o hacer commit.
- No incluir `.env`, secretos, dumps de base de datos ni artefactos generados pesados.

## Decisiones tecnicas activas

- El interceptor HTTP de Angular agrega `?email=<email>` y `Authorization: Bearer <token>` a todas las peticiones `/api/v1` que no sean de autenticacion. Esto es el mecanismo de identidad en modo desarrollo.
- `ListaOfertasComponent` usa Angular Signals para estado local (`loading`, `error`, `mensaje`, `ofertas`, `postuladas`, `showPerfilModal`). La peticion `getOfertasActivas()` NO usa `takeUntilDestroyed` deliberadamente: si el usuario navega fuera, la peticion huerfana en background calienta la conexion HikariCP/Supabase para la siguiente visita. `getMisPostulaciones()` si usa `takeUntilDestroyed` porque es una carga secundaria sin impacto en la fiabilidad de la conexion.
- HikariCP esta configurado en `application-dev.yml` con `keepalive-time`, `validation-timeout`, `connection-test-query` y `connection-timeout` reducido para detectar rapido conexiones muertas con el pooler de Supabase. El backend debe reiniciarse para que estos valores sean efectivos.
- Retirar candidatura: endpoint `DELETE /api/v1/postulaciones/{id}` permite al postulante eliminar fisicamente una postulacion propia en estado `PENDIENTE`. No hay estado `CANCELADA` ni `RETIRADA` en la BD; es un hard delete.

## Prohibiciones explicitas

- Prohibido tocar secretos reales.
- Prohibido inventar tablas, endpoints o roles.
- Prohibido asumir que la seguridad de produccion esta activa: el `SecurityConfig` actual permite todas las peticiones.
- Prohibido tomar como implementado lo que solo aparece comentado como modo produccion.
