# Chat empresa-postulante

## Estado

No implementado.

Esta es una propuesta tecnica para analizar antes de crear tablas, endpoints o componentes. No debe implementarse hasta cerrar el flujo principal del MVP web.

## Entidades actuales relacionadas

La entidad natural para iniciar un chat es `Postulacion`, porque ya conecta:

- `PostulanteProfile`
- `OfertaTrabajo`
- `EmpresaProfile`
- Estado de la candidatura

Tambien permite controlar que solo hablen empresa y postulante vinculados por una candidatura real.

## Conviene que el chat nazca desde una postulacion

Propuesta: si.

Ventajas:

- Evita conversaciones sin contexto.
- Facilita mostrar chat desde "Mis postulaciones" y "Postulantes de una oferta".
- Permite permisos claros: solo empresa propietaria de la oferta y postulante que aplico.
- Facilita evolucion futura: abrir chat solo si la postulacion esta `ACEPTADA` o permitirlo desde `PENDIENTE`, segun decision de producto.

Pendiente de confirmar:

- Si el chat debe estar disponible al postularse o solo tras aceptar candidatura.
- Si admin podra auditar conversaciones.

## Modelo de datos propuesto

Propuesta minima:

- `chat_conversation`
  - `id`
  - `postulacion_id`
  - `created_at`
  - `updated_at`

- `chat_message`
  - `id`
  - `conversation_id`
  - `sender_user_id`
  - `body`
  - `read_at`
  - `created_at`

No crear estas tablas todavia. Antes hay que decidir permisos, lifecycle y migraciones.

## Endpoints REST propuestos

MVP con polling:

- `GET /api/v1/postulaciones/{postulacionId}/chat`
  - Devuelve o crea conversacion para esa postulacion.

- `GET /api/v1/chats/{conversationId}/mensajes?page=&size=`
  - Lista mensajes paginados.

- `POST /api/v1/chats/{conversationId}/mensajes`
  - Crea mensaje.

- `PUT /api/v1/chats/{conversationId}/leido`
  - Marca mensajes como leidos para usuario actual.

Compatibilidad Flutter:

- Respuestas con DTOs simples y paginacion estable.
- No depender de detalles Angular.
- Identidad desde JWT, no desde `email` query param.

## Componentes Angular posibles

Crear cuando toque implementar:

- `ChatThreadComponent`: lista de mensajes y caja de texto.
- `ChatButtonComponent`: boton "Contactar" o "Abrir chat" reutilizable en postulacion/oferta.
- Integracion en:
  - `/postulante/postulaciones`
  - `/empresa/ofertas/:ofertaId/postulaciones`

Reutilizable existente:

- `TokenStorageService`
- `authInterceptor`
- `PostulacionService`
- rutas por rol
- estructura de componentes standalone

## MVP simple con polling

Recomendado para primera version:

- REST + polling cada 5-10 segundos cuando la pantalla esta abierta.
- Paginacion simple.
- Sin WebSockets.
- Menos infraestructura y menos riesgo para lanzamiento.

Riesgos:

- Mas peticiones si hay muchas conversaciones abiertas.
- Sensacion menos instantanea.

## Futuro con WebSockets

Propuesta futura:

- Spring WebSocket/STOMP o alternativa basada en Supabase Realtime si se decide.
- Notificaciones en tiempo real.
- Indicador de escribiendo/leido.

No bloquear MVP con WebSockets.

## Primera decision pendiente

Decidir regla de producto:

- Opcion A: permitir chat desde que existe postulacion `PENDIENTE`.
- Opcion B: permitir chat solo cuando empresa marca `ACEPTADA`.

Recomendacion inicial: opcion A si se busca conversion rapida y contacto facil; opcion B si se quiere reducir spam y ruido.

