# Roadmap

## Enfoque actual

Liftnet debe avanzar hacia un MVP web lanzable para empresas y postulantes del sector socorrismo. No se debe reescribir lo existente: la prioridad es cerrar el flujo principal usando los modulos ya desarrollados.

## Estado actual

Implementado:

- Backend Spring Boot con dominios principales creados.
- API versionada bajo `/api/v1`.
- Login/registro con JWT.
- Roles `POSTULANTE`, `EMPRESA`, `ADMIN`.
- Perfiles de postulante y empresa.
- Ofertas de trabajo.
- Postulaciones y estados `PENDIENTE`, `ACEPTADA`, `RECHAZADA`. Retirar candidatura implementado (hard delete, solo PENDIENTE).
- Experiencias, certificaciones y CV/fotos via Supabase Storage.
- Frontend Angular con rutas, guards, servicios y stores.
- App Flutter inicial con auth/feed/ofertas/perfil parcialmente creado.
- PostgreSQL/Supabase configurado; en local funciona mediante pooler.

Parcial:

- Seguridad backend real: `SecurityConfig` activo usa `permitAll`.
- Identidad: mezcla de `email` query param y `SecurityContext`.
- Respuestas API no estan uniformadas.
- Flutter no esta alineado todavia con contratos reales del backend.
- Admin es basico.
- Flyway configurado sin migraciones.
- Entidades JPA y esquema Supabase tienen diferencias documentadas.

## Fase 1: Cerrar flujo web empresa-postulante

Objetivo: que el MVP web sea usable de punta a punta.

Prioridad alta:

- Verificar y corregir flujo login/registro/perfil por rol.
- Verificar navegacion tras guardar perfil empresa hacia `/empresa/ofertas/mis-ofertas`.
- Validar crear/editar/cerrar ofertas.
- Validar ver ofertas activas desde rol adecuado.
- Validar postulacion y evitar duplicados con error claro. Corregido: duplicados devuelven `409 Conflict` y Angular muestra estado "ya inscrito".
- Validar empresa ve candidatos y cambia estado.
- Revisar admin para que no bloquee MVP.
- Mantener `.env` fuera de Git y pooler documentado.

Tareas completadas en Fase 1:

- Navegacion `EmpresaProfileComponent` corregida hacia `/empresa/ofertas/mis-ofertas`.
- Ruta `/ofertas` accesible para postulantes sin requerir perfil completo.
- Error de postulacion duplicada alineado con `409 Conflict` y mensaje Angular.
- Postulaciones precargadas en `/ofertas` con estado y id para mostrar "Ya inscrito" / "Retirar candidatura".
- Retirar candidatura implementado (`DELETE /api/v1/postulaciones/{id}`, solo estado `PENDIENTE`, hard delete).
- Bug de duplicado en guardar perfil postulante corregido (`profileExistsInDB` signal en store).
- Carga fiable de `/ofertas`: `timeout(8000)`, `retry`, HikariCP configurado para Supabase pooler, signals para estado local.

## Fase 2: UX y diseño visual

Plan incremental definido (auditoria UX/UI completada 2026-06-01):

Fase 0 — Fundacion global (IMPLEMENTADA 2026-06-01):
- Variables CSS en `:root`: paleta completa (primary, accent, danger, success, warning, info), texto, fondos, sombras, radios, spacing, tipografia.
- Reset minimo (`box-sizing`, `body`, `font-family`) aplicado en `styles.css`.
- Clases globales reutilizables: `.btn`, `.btn-primary/secondary/accent/danger/outline-primary`, `.form-control`, `.form-label`, `.form-group`, `.card`, `.card-elevated`, `.alert-*`, `.badge-*`, `.empty-state`, `.spinner`.
- Build limpio confirmado (`npm run build`).

Fase 1 — Login/Register (IMPLEMENTADA 2026-06-01):
- Pagina centrada con fondo degradado azul/slate elegante.
- Card blanca elevada con sombra xl, border-radius 16px, padding generoso.
- Logo/branding Liftnet con icono en card superior.
- Inputs modernos con focus ring azul, placeholder gris.
- Boton submit con sombra, hover y estado disabled.
- Error con alert rojo inline.
- Footer con link estilizado.
- Register con select de rol estilizado (chevron custom).
- Totalmente responsive (mobile 480px).
- Logica Angular sin cambios; solo HTML/CSS.

Fase 2 — Navbar (IMPLEMENTADA 2026-06-01):
- Fondo blanco, border-bottom slate, shadow sutil, position sticky.
- Logo con icono SVG Liftnet + texto bicolor (negro/azul).
- Links con hover azul claro y estado active via routerLinkActive.
- Badge de rol coloreado por tipo (POSTULANTE/EMPRESA/ADMIN).
- Logout como botón outline gris → rojo en hover.
- Responsive: badge oculto en mobile, logo-text oculto en 480px.
- Reutiliza variables CSS globales de styles.css.

Fase 3 — `/ofertas` (IMPLEMENTADA 2026-06-01):
- Todos los colores hardcoded reemplazados por variables CSS globales.
- Spinner real (`.spinner.spinner-lg`) en loading state en lugar de texto plano.
- Empty state con icono 🏊 y subtexto orientativo.
- Alerts success/error con variables de color globales.
- Cards de oferta con `var(--shadow-sm/lg)`, `var(--color-border)`, hover elevado.
- Titulo de oferta en `var(--color-primary)`, salario en verde variable.
- Botones `btn-postular` (accent naranja), `btn-retirar` (danger), `btn-reintentar` (primary).
- Modal con `var(--shadow-xl)` y border-radius 16px.
- Budget de componentes ajustado a 8 kB warning / 16 kB error en `angular.json`.
- Build limpio sin warnings.

Fase 4 — Home (IMPLEMENTADA 2026-06-01):
- Hero de marketing para usuarios no autenticados: degradado azul, eyebrow, titulo con accent dorado, subtitulo, CTAs "Crear cuenta gratis" / "Iniciar sesion".
- Hero compacto para usuarios autenticados.
- Spinner real en loading state.
- Estado error visible (antes el signal existia pero nunca se mostraba en HTML).
- Empty state con icono y subtexto.
- Inline styles eliminados completamente (3 instancias).
- Cards de ofertas con variables globales: hover border primary, sombra, titulo azul, salario verde.
- Boton "Ver detalles" en azul claro, link "Mis postulaciones" solo visible para POSTULANTE autenticado.
- Modal mejorado: info chips en pills slate, descripcion con color variable, mensajeExito/Error con clases CSS.
- Null-guard en modal (`*ngIf="showModal && ofertaSeleccionada"`).
- Build limpio sin warnings.

Fase 5 — Perfiles POSTULANTE y EMPRESA (IMPLEMENTADA 2026-06-01):
- Todos los colores hardcoded reemplazados por variables CSS globales en ambos componentes.
- Inline styles eliminados del HTML (8 instancias en postulante, 1 en empresa).
- Avatar postulante: border con `--color-primary-light`, upload-badge azul primario.
- Avatar empresa: border-radius 14px corporativo, upload-badge azul primario.
- Progress bar: color `--color-primary` unificado en ambos (antes verde hardcoded en postulante, verde en empresa).
- Cards: `--shadow-xs`, `--color-border`, header uppercase con letter-spacing.
- Edit card header azul claro `--color-primary-light` en ambos (antes verde en postulante).
- Formularios: `.form-control` con focus ring azul, inputs alineados con Login/Register.
- Alertas: variables globales de color en ambos perfiles.
- Clases semanticas nuevas en postulante: `.cv-file-name`, `.exp-desc`, `.empty-state-danger`, `.cv-upload-section`, `.cv-upload-label`, `.cv-exists-notice`, `.cv-uploading-hint`.
- Loading screen: spinner con `--color-primary` en ambos.
- Redirect post-login POSTULANTE corregido: `auth.service.ts` línea 106 cambiada de `/postulante/postulaciones` a `/ofertas`.
- Redirect `/`: guard `homeRedirectGuard` creado en `core/guards/home-redirect.guard.ts`.
  - Sin sesion → Home publica.
  - POSTULANTE → `/ofertas`.
  - EMPRESA → `/empresa/ofertas/mis-ofertas`.
  - ADMIN → `/admin`.
- Build: cero errores. 1 warning de budget en postulante-profile (8.93 kB sobre limite 8 kB, justificado por riqueza del componente).

Fase 6 — `/empresa/ofertas/mis-ofertas` (IMPLEMENTADA 2026-06-01):
- Cabecera de pantalla con titulo + subtitulo (antes solo h2 con color hardcoded #1976d2).
- Spinner real en loading state (antes texto plano en cursiva).
- Alertas con variables globales de color.
- Card de creacion con header visual (icono + titulo), border-bottom separador.
- Formulario: `form-control` añadido a todos los inputs y textareas (antes sin clase).
- Focus ring azul con variable `--color-primary` (antes #0284c7 hardcoded).
- Boton publicar full-width con shadow y hover (clase `btn-publish`).
- Cards de oferta: `--shadow-xs`, hover border gris, padding consistente.
- Acciones con separador `border-top` visible.
- Botones: sistema de 4 variantes con clases semanticas (`btn-action-primary/outline/neutral/danger`).
- Anti-patron `<a><button>` corregido: "Ver postulantes" ahora es un `<a>` con clase de boton.
- Badges: `abierta` verde con variables, `cerrada-badge` slate con variables.
- Tags de metadatos: variables globales de color y border.
- Contador de ofertas en el titulo de seccion.
- Empty state con icono 📋 y subtexto orientativo.
- Formulario de edicion inline con mismo sistema de clases.
- Responsive: acciones en columna y botones full-width en mobile.
- Build limpio: cero errores, un warning preexistente.

Fase 7 — Empresa postulaciones (IMPLEMENTADA 2026-06-01):
- Cabecera con titulo + subtítulo descriptivo.
- Spinner `.spinner` en loading state (antes solo texto).
- Loading panel con borde izquierdo azul primario.
- Error panel con fondo y borde rojo con variables globales + botón reintentar azul.
- Empty state con icono 👥 + subtexto orientativo.
- Badges estado: pendiente amarillo, aceptada verde, rechazada rojo — todos con variables.
- Acento lateral de card por estado via `border-left-color` con variables.
- Barra CV con `var(--color-info-light/border)` y subtexto con clase `.cv-text-sub`.
- Inline style eliminado del HTML (1 instancia: `display:block; font-weight:normal; font-size:0.85em`).
- Mini-CV: títulos uppercase + letter-spacing, items con `--color-bg-card` y `--color-border`.
- Botones mini PDF: `var(--color-info-*)` para ver, `var(--color-success-*)` para descargar.
- Btn aceptar: sólido verde con sombra verde, `var(--color-success)`.
- Btn rechazar: outline rojo suave → rojo sólido en hover, `var(--color-danger-*)`.
- Orden visual mejorado: "Rechazar" a la izquierda, "Aceptar" a la derecha (convención positiva derecha).
- Responsive: header en columna, acciones apiladas, botones full-width en mobile.

Fase 8 — Postulante: mis candidaturas (IMPLEMENTADA 2026-06-01):
- Cabecera de pagina con titulo "Mis candidaturas" y subtitulo descriptivo.
- Spinner real `.spinner.spinner-lg` en loading state (antes texto plano).
- Estado de error con `.alert.alert-danger` + boton reintentar `.btn.btn-secondary.btn-sm`.
- Empty state con `.empty-state` global: icono 📋, titulo, subtexto y boton `.btn.btn-primary` a `/ofertas`.
- Cards con clase `.card` global; borde izquierdo de color por estado (warning/success/danger).
- Cabecera de card con titulo de oferta en `--color-primary`, nombre empresa, badge de estado en fila.
- Badges reutilizando `.badge.badge-warning/success/danger` globales.
- Descripcion de estado como texto muted bajo los datos de la oferta.
- Fecha de postulacion con `.text-muted` y `.font-size-xs`.
- Boton retirar candidatura: `.btn.btn-danger.btn-sm` + separador border-top.
- Caja de contacto empresa (estado ACEPTADA): fondo `--color-success-light`, border `--color-success-border`, lista con iconos.
- Todos los colores hardcoded reemplazados por variables CSS globales.
- Responsive mantenido (mobile 600px).
- Build limpio: cero errores, un warning preexistente (postulante-profile).

Fase 9 — Postulante: experiencias y certificaciones (IMPLEMENTADA 2026-06-01):
- Cabecera de página con titulo + subtitulo en ambas pantallas.
- Card de formulario con header visual azul claro (icono + texto) y borde inferior separador.
- Spinner real `.spinner.spinner-lg` en loading state (antes `<p>` de texto).
- Empty state con `.empty-state` global: icono 💼/🏅, titulo, subtexto.
- Alertas globales: `.alert.alert-success` / `.alert.alert-danger` (antes clases locales `.alert.success/.error`).
- Botón submit: `.btn.btn-primary.btn-full` (antes `.btn-submit` custom hardcoded #0a66c2).
- Inline style eliminado de mis-experiencias (`style="color:#666;font-size:0.8em..."`).
- Cards con `.card` global + layout flex component-specific.
- Hover en cards con `var(--shadow-md)` y `translateY(-1px)`.
- Botones Ver/Descargar/Eliminar: `.btn.btn-sm` + variantes semánticas con variables CSS.
- Ver: fondo `--color-info-light`, borde `--color-info-border`.
- Descargar: fondo `--color-success-light`, borde `--color-success-border`.
- Eliminar: outline `--color-danger-border` → rojo sólido en hover.
- Etiquetas `label-required` (rojo) y `label-optional` (muted gris) para claridad visual.
- CSS de certificaciones: eliminada duplicación de reglas (tenía `.cert-card` y `.cert-actions` definidas dos veces con valores en conflicto).
- Todos los colores hardcoded reemplazados por variables CSS globales.
- Responsive mantenido (600px: columnas apiladas, botones al final).
- Build limpio: cero errores, un warning preexistente (postulante-profile).

Refuerzo auth validaciones (2026-06-01):
- Backend: `@Size(min=8, message="La contraseña debe tener al menos 8 caracteres")` en `RegisterRequest.password`.
- Backend: mensajes de excepción traducidos al español: `EmailAlreadyExistsException` → "El email ya está registrado"; `InvalidCredentialsException` → "Credenciales incorrectas".
- Backend: `GlobalExceptionHandler.handleValidationErrors` ahora devuelve el primer mensaje de campo concreto en lugar del genérico "Error de validación", para que Angular lo muestre directamente al usuario.
- Frontend: `minlength="8"` en el campo password del register; error inline con `.form-error` (clase global) visible al tocar el campo y no cumplir el mínimo.
- Pendiente: traducir `DataIntegrityViolationException` y otros errores genéricos. Pendiente: validación visual inline para `email` y `confirmPassword`. Pendiente: `@Size` en `AuthRequest.password` (login no necesita validación de longitud pero sí coherencia).

Auditoría registro (2026-06-01): flujo completamente funcional de extremo a extremo. Causa del problema observado: el handler de error en RegisterComponent mostraba siempre "No se pudo completar el registro" ignorando el mensaje real del backend (email duplicado, validación fallida, etc.). Corregido: ahora extrae `err.error.message` y lo muestra directamente. Un solo cambio en `register.component.ts`.

Pendiente para despues de UX visual:
- Formularios con validaciones visibles.
- Mensajes de error consistentes y homogeneos.
- Textos orientados a socorristas.
- Filtros basicos de ofertas.
- Reducir `any` en servicios Angular.
- Unificar `ApiResponse` en controladores.

## Fase 3: Chat basico empresa-postulante

- Disenar conversacion ligada a `Postulacion`.
- Implementar REST + polling.
- Crear componentes Angular de hilo de chat.
- Permisos por postulacion.
- Preparar contrato para Flutter.

WebSockets quedan para una fase futura.

## Fase 4: Admin mas completo

- Ajustar lista de usuarios paginada.
- Gestion de ofertas desde admin.
- Busqueda y filtros de usuarios.
- Auditoria basica de acciones.
- Gestion de incidencias/reportes si aparece la necesidad.

## Fase 5: Preparacion Flutter/app movil

- Estabilizar DTOs y respuestas paginadas.
- Alinear rutas Flutter con backend real.
- Reusar endpoints de login/perfil/ofertas/postulaciones/chat.
- Implementar postulaciones en app.
- Decidir storage y subida de documentos en movil.

## Riesgos transversales

- Activar seguridad real demasiado tarde puede obligar a tocar muchos endpoints.
- Mantener `email` query param puede bloquear Flutter y produccion.
- Crear chat antes de cerrar postulaciones puede introducir deuda.
- No tener migraciones Flyway complica despliegue y reproducibilidad.
