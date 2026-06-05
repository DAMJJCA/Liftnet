# MVP web Liftnet

## Objetivo

Lanzar cuanto antes una version web usable para empresas y postulantes en el sector de socorristas. El MVP debe permitir que una empresa publique ofertas, que un postulante se registre, complete su perfil, vea ofertas, se postule y que la empresa gestione candidaturas.

`ADMIN` se mantiene como soporte operativo, pero no bloquea el lanzamiento inicial. Flutter queda para despues, reutilizando la misma API.

## Flujo principal estado actual

### Registro/login

Implementado:

- Angular tiene `/register` y `/login`.
- Backend tiene `POST /api/v1/auth/register` y `POST /api/v1/auth/login`.
- Se guarda token, email, rol y `profileCompleted` en localStorage.

Parcial:

- El registro no crea perfil vacio aunque existen metodos backend preparados.
- No hay endpoint backend `/auth/set-role`, aunque existe metodo Angular `setRole`.
- Validacion de inputs y mensajes de error son basicos.

### Perfil de postulante

Implementado:

- Ruta `/postulante/perfil`.
- Ver/crear/actualizar datos basicos.
- Foto y CV via Supabase Storage.
- Experiencias y certificaciones se cargan junto al perfil.

Parcial:

- `profileCompleted` depende de localStorage y respuesta de login.
- Validacion de campos y UX de errores mejorables.

Corregido (2026-05-31):

- Bug de duplicado al guardar perfil resuelto: `PostulanteProfileStore` ahora usa `profileExistsInDB` (actualizado desde la respuesta real del backend) en vez de `profileCompleted` de localStorage para decidir POST vs PUT. El escenario roto era: perfil en BD con `nombre` vacio → login devuelve `profileCompleted=false` → Angular llamaba a `createProfile` aunque el perfil ya existia → backend devolvía 400 → usuario bloqueado en `/postulante/perfil` y nunca podia llegar a `/ofertas`.

### Perfil de empresa

Implementado:

- Ruta `/empresa/perfil`.
- Ver/crear/actualizar perfil.
- Logo/foto via Supabase Storage.

Parcial:

- Backend usa `SecurityContext`, mientras otros endpoints usan `email` query param.
- Navegacion tras crear perfil corregida para llevar a `/empresa/ofertas/mis-ofertas`.

### Crear ofertas

Implementado:

- Ruta `/empresa/ofertas/mis-ofertas` permite crear, editar y cerrar ofertas.
- Backend valida que la oferta pertenece a la empresa para editar/cerrar.
- Campos actuales: titulo, descripcion, ubicacion, estudios minimos, experiencia minima, idiomas, nivel, vacantes, salario.

Parcial:

- No estan modelados en entidad JPA `fecha_inicio` y `fecha_fin`, aunque existen en Supabase.
- Validaciones de formulario basicas.

### Ver ofertas

Implementado:

- Ruta `/empresa/ofertas/lista` lista ofertas activas.
- Ruta `/ofertas` reutiliza la lista de ofertas activas para `POSTULANTE`.
- La ruta `/ofertas` es accesible aunque el perfil del postulante no esté completo (`profileGuard` eliminado de esta ruta).
- Mientras carga muestra "Buscando ofertas..."; el estado vacío solo aparece tras terminar la carga.
- Las dos llamadas HTTP están desacopladas: `getOfertasActivas()` y `getMisPostulaciones()` se lanzan en paralelo pero de forma independiente. Las ofertas se muestran en cuanto responde la primera; las marcas "Ya inscrito" se actualizan cuando responde la segunda (sin bloquear la principal).
- `getOfertasActivas()` usa `timeout(8000)` + `retry({ count: 2, delay: 800 })`. Deliberadamente NO usa `takeUntilDestroyed`: si el usuario navega fuera antes de que responda, la peticion huerfana sigue en background y calienta la conexion HikariCP/Supabase, haciendo que la siguiente visita a `/ofertas` cargue en < 1s.
- `getMisPostulaciones()` usa `timeout(8000)` + `takeUntilDestroyed`: es una carga secundaria (solo para marcar "Ya inscrito"), puede cancelarse sin consecuencias para la fiabilidad.
- Estado local del componente usa Angular Signals: `loading`, `error`, `mensaje`, `ofertas`, `postuladas`, `showPerfilModal`. Garantiza actualizacion reactiva sin trucos de referencia forzada (elimina el anterior `new Map(this.postuladas)`).
- Si `getOfertasActivas()` falla definitivamente (3 intentos), muestra error con botón "Reintentar".
- Backend: HikariCP en `application-dev.yml` con `validation-timeout: 3000`, `connection-test-query: SELECT 1` y `connection-timeout: 8000` para detectar rápido conexiones muertas con Supabase y fallar en < 8s. **Requiere reinicio del backend para ser efectivo.**
- Si el perfil no está completo y el postulante pulsa "Inscribirme", aparece un modal con botón "Completar mi perfil" → `/postulante/perfil`.
- Backend permite filtrar por ubicacion.

Implementado (2026-06-05):

- Filtros básicos en `/ofertas`: ubicación (backend, `?ubicacion=`) y nivel (frontend, en memoria).
- Barra de filtros con botón "Buscar", "Limpiar" y hint informativo de filtros activos.
- Empty-state diferenciado: "Sin resultados para esta búsqueda" vs "No hay ofertas disponibles".

Parcial:

- Se mantiene tambien la ruta antigua bajo `empresa`; conviene ordenar rutas en una fase posterior.
- Filtros adicionales pendientes: temporada, piscina/playa, titulacion, jornada, salario numérico, disponibilidad (requieren soporte backend o campos estructurados en el modelo).

### Postularse a ofertas

Implementado:

- `POST /api/v1/postulaciones/oferta/{ofertaId}`.
- Backend impide postularse a oferta inactiva.
- Backend revisa duplicados con `existsByOfertaAndPostulante`.
- Backend devuelve `409 Conflict` si el postulante ya estaba inscrito.
- Angular muestra mensaje de duplicado y bloquea el boton en la lista de ofertas tras exito o `409`.
- La ruta `/ofertas` precarga las postulaciones del usuario y marca como "Ya inscrito" las ofertas ya postuladas desde el primer render.

Parcial:

- La restriccion unica existe en JPA pero no aparece en esquema Supabase aportado.
- La precarga actual pide hasta 100 postulaciones; si en el futuro hay mas volumen, conviene paginar o exponer un endpoint ligero de ids de ofertas postuladas.

### Empresa ve postulantes

Implementado:

- Ruta `/empresa/ofertas/:ofertaId/postulaciones`.
- Backend devuelve datos del postulante, experiencias, certificaciones y CV.
- Empresa puede aceptar/rechazar.

Parcial:

- Faltan acciones de contacto/comunicacion.
- Falta vista detallada pulida de candidato.

### Admin ve usuarios/ofertas

Implementado:

- Admin puede listar usuarios, cambiar rol y activar/desactivar.

Pendiente:

- No hay gestion de ofertas en admin detectada.
- Posible desajuste entre respuesta paginada backend y array esperado frontend.

### Estados de postulacion

Implementado:

- `PENDIENTE`, `ACEPTADA`, `RECHAZADA`.
- Empresa puede cambiar a `ACEPTADA` o `RECHAZADA`.

Implementado (2026-06-01):

- Retirar candidatura desde `/ofertas` y `/postulante/postulaciones`.
- Solo disponible en estado `PENDIENTE`. Si está `ACEPTADA` o `RECHAZADA`, el botón no aparece.
- Hard delete: la postulación se elimina físicamente. Sin cambios en el esquema PostgreSQL.
- Backend: `DELETE /api/v1/postulaciones/{postulacionId}` — valida propietario y estado `PENDIENTE`.
- En `/ofertas`: `postuladas` es ahora un `Map<ofertaId, {postulacionId, estado}>` para saber el estado de cada inscripción. Tras retirar, el botón vuelve a "Inscribirme a esta oferta".
- En `/postulante/postulaciones`: botón "Retirar candidatura" en tarjetas con estado `PENDIENTE`. Tras retirar, la tarjeta desaparece de la lista.

Pendiente/propuesta:

- Historial de cambios de estado.
- Notificaciones.

## Criterio de MVP lanzable

Alta prioridad:

- Confirmar que login/registro/perfiles/ofertas/postulaciones funcionan de punta a punta.
- Corregir navegacion tras crear perfil empresa.
- Resolver desajuste de admin paginado si se mantiene visible.
- Alinear errores de duplicado de postulacion con mensaje frontend.
- Mantener backend arrancando con `.env` y pooler Supabase.

Media prioridad:

- Mejorar textos y UX orientados a socorristas.
- Mejorar validaciones visibles.
- Homogeneizar respuestas para reducir `any`.
- Preparar contrato API para Flutter.

No bloqueante para MVP:

- Chat.
- Admin avanzado.
- Flutter completo.
- WebSockets.
- IA/matching.
