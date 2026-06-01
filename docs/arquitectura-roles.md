# Arquitectura de roles

## Roles encontrados

Roles definidos en `Role.java`:

- `POSTULANTE`
- `EMPRESA`
- `ADMIN`

Estos roles tambien aparecen en rutas Angular y en anotaciones `@PreAuthorize` de algunos controladores o bloques comentados.

## Estado real de autorizacion

Importante: aunque hay roles, guards frontend y anotaciones `@PreAuthorize`, el `SecurityConfig` activo permite todas las peticiones. Por tanto, la autorizacion fuerte en backend no esta completamente activa.

Algunos endpoints usan `email` por query param en modo desarrollo para identificar al usuario. Esto no debe considerarse seguro para produccion.

## POSTULANTE

Implementado o detectado:

- Acceso web a `/postulante/perfil`.
- Acceso web a postulaciones, experiencias y certificaciones tras `profileGuard`.
- Puede ver ofertas activas desde `GET /api/v1/ofertas`.
- Puede crear/actualizar perfil de postulante en endpoints de perfil.
- Puede cambiar disponibilidad.
- Puede gestionar experiencias.
- Puede gestionar certificaciones asignadas.
- Puede postularse a ofertas.
- Puede consultar sus postulaciones.
- Puede retirar su candidatura si el estado es `PENDIENTE` (hard delete via `DELETE /api/v1/postulaciones/{id}`). No puede retirar si el estado es `ACEPTADA` o `RECHAZADA`.

Pendiente de confirmar:

- Si las ofertas deben ser visibles publicamente o solo para `POSTULANTE`.
- Validacion backend efectiva de que el email enviado por query param pertenece al token.

## EMPRESA

Implementado o detectado:

- Acceso web a `/empresa/perfil`.
- Acceso web a ofertas de empresa tras `profileGuard`.
- Puede crear/actualizar perfil de empresa.
- Puede crear ofertas.
- Puede editar ofertas.
- Puede ver sus ofertas.
- Puede cerrar ofertas.
- Puede ver postulaciones recibidas para sus ofertas.
- Puede actualizar estado de postulaciones.

Pendiente de confirmar:

- En `EmpresaController` se usa `SecurityContext`, pero la seguridad global activa no esta autenticando JWT.
- En otros endpoints de empresa se usa `email` por query param en modo desarrollo.

## ADMIN

Implementado o detectado:

- Ruta web `/admin` protegida por `authGuard` y `roleGuard` en Angular.
- Endpoints backend bajo `/api/v1/admin`.
- Puede listar usuarios.
- Puede cambiar rol de usuario.
- Puede habilitar/deshabilitar usuario.

Pendiente de confirmar:

- Panel admin real en frontend y alcance funcional.
- Auditoria de acciones administrativas.
- Politica para crear el primer usuario ADMIN.

## Permisos previstos por rol

Los permisos previstos aparecen parcialmente en comentarios de modo produccion:

- `POSTULANTE`: endpoints de perfil de postulante, experiencias, certificaciones, ver ofertas y postularse.
- `EMPRESA`: endpoints de perfil de empresa, ofertas propias y postulaciones recibidas.
- `ADMIN`: endpoints administrativos de usuarios.

No tomar estos permisos como plenamente activos hasta activar y probar Spring Security con JWT.

## Propuestas marcadas como propuesta

Propuesta:

- Aplicar autorizacion en backend con `@PreAuthorize` y token JWT real, eliminando `email` por query param.
- Usar claims de JWT para resolver identidad en todos los servicios.
- Mantener guards frontend solo como ayuda de UX; la autorizacion final debe estar en backend.

