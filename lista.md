# Lista de fallos — WindMill Weather

Documento de trabajo con todos los hallazgos del análisis. Sirve para revisar uno a uno y decidir qué corregir, qué dejar y qué revertir.

**Cómo pedirme cambios:** usa los IDs. Ejemplos: `corrige F12, F14 y F15`, `no toques L5`, `revierte F7`, `deja W3 como está`.

## Leyenda de estado

| Estado | Significado |
|---|---|
| ✅ | Corregido en el código actual |
| 🟡 | Parcial (parte del problema sigue) |
| ⬜ | Pendiente (no se ha tocado) |
| 🔍 | No verificado / requiere prueba |

---

## Resumen por prioridad

### Críticos y graves (F1–F10)

| ID | Sev. | Estado | Hallazgo |
|---|---|---|---|
| F1 | Crítica | ✅ | La AppBar se dibuja bajo la barra de estado tras recrear (tema/idioma/fuente). |
| F2 | Crítica | ✅ | El cambio ES/GL no se aplica hasta cerrar y reabrir la app. |
| F3 | Alta | ✅ | Durante carga/error se muestran `0.0ºC`, `00%`, `ESTACIÓN` como si fueran datos reales. |
| F4 | Alta | ✅ | Iconos de viento blancos sobre tarjeta clara: casi invisibles. |
| F5 | Alta | ✅ | Contrastes insuficientes en pestañas, lluvia y textos secundarios. |
| F6 | Alta | ✅ | Touch targets <48 dp: badge ES/GL y spinners de ubicación. |
| F7 | Alta | ✅ | El título se solapa con badge/acciones en pantallas pequeñas o fuente grande. |
| F8 | Alta | ✅ | Bandas blancas a los lados de las pestañas en landscape/tablet. |
| F9 | Alta | ✅ | Cielo/viento sin descripción real; temperaturas sin etiqueta Máx./Mín. |
| F10 | Alta/Media | ✅ | Termómetros con fondo blanco opaco (cuadrados blancos). |

### Medios y bajos (F11–F21)

| ID | Sev. | Estado | Hallazgo |
|---|---|---|---|
| F11 | Media | ✅ | Textos de 8–10 sp demasiado pequeños (etiquetas y widget). |
| F12 | Media | ✅ | Diálogos de provincia/localidad sin título, sin marca de selección y sin búsqueda. |
| F13 | Media | ✅ | Al cambiar de tema se pierde la pestaña seleccionada y se vuelve a descargar. |
| F14 | Media | ✅ | Error sin conexión: diálogo no cancelable, sin datos guardados ni cambio de ubicación. |
| F15 | Media | ✅ | Al cargar otra localidad se siguen mostrando cabecera y datos anteriores. |
| F16 | Media | ✅ | Hueco vacío bajo las temperaturas porque el ProgressBar usa `INVISIBLE`. |
| F17 | Media/Baja | ⬜ | “PASADO” es ambiguo (¿pasado o pasado mañana?). |
| F18 | Baja | ✅ | `Predicción:09-11-2026` sin espacio tras los dos puntos. |
| F19 | Baja/Media | ✅ | La mínima en gris y sin etiquetas Máx./Mín.; depende solo de color/icono. |
| F20 | Baja/Media | ✅ | Última tarjeta pegada a la barra de gestos (sin inset inferior). |
| F21 | Media | ✅ | Widget: textos pequeños, etiqueta `tomorrow`, ciudad de ejemplo y descripciones genéricas. |

### Widget (W1–W9)

| ID | Sev. | Estado | Hallazgo |
|---|---|---|---|
| W1 | Media | ✅ | La franja de la mañana usaba `@string/tomorrow` (“MAÑANA”) en vez de `morning`. |
| W2 | Alta | ✅ | El widget esperaba a descargar todas las imágenes antes de publicar textos; sin timeouts. |
| W3 | Media | ✅ | El widget no sigue del todo el modo oscuro in-app. |
| W4 | Media | ✅ | Textos muy pequeños: fecha 8 sp, periodos 9 sp, lluvia 10 sp, estado 11 sp. |
| W5 | Baja | ✅ | El widget pequeño solo mostraba la temperatura máxima. |
| W6 | Media | ✅ | Si faltaba precipitación mostraba `00%` en vez de `--%`. |
| W7 | Baja | ✅ | `widgetFeatures="reconfigurable"` sin actividad de configuración. |
| W8 | Baja | ✅ | El selector de widgets no tenía `previewLayout`/`previewImage`. |
| W9 | Media | ✅ | Iconos de cielo con descripciones genéricas en vez de la condición real. |

### Accesibilidad (A1–A6)

| ID | Sev. | Estado | Hallazgo |
|---|---|---|---|
| A1 | Alta | ✅ | Toggle de idioma sin `contentDescription` ni rol/estado. |
| A2 | Alta | ✅ | Cielo y viento solo imagen, sin alternativa textual real. |
| A3 | Media | ✅ | Temperaturas sin etiqueta de máxima/mínima para TalkBack. |
| A4 | Media | ✅ | Pestañas sin rol ni posición (“pestaña 1 de 3”). |
| A5 | Baja | ✅ | GIF del splash expuesto a TalkBack y 3 s fijos. |
| A6 | Baja | ✅ | Botón de tema sin indicar el estado actual (claro/oscuro). |

### Responsive (L1–L7)

| ID | Sev. | Estado | Hallazgo |
|---|---|---|---|
| L1 | Alta | ✅ | En 320 dp la temperatura mínima se parte en dos líneas y el título se trunca. |
| L2 | Alta | ✅ | En 320 dp + fuente 1,3 empeora el wrap y el título. |
| L3 | Media | ✅ | En tablet 800 dp las pestañas quedan centradas con bandas blancas. |
| L4 | Media | ✅ | Al hacer scroll el título queda bajo la barra de estado, ilegible. |
| L5 | Baja | ✅ | En tablet todo va en una sola columna con mucho espacio vacío. |
| L6 | Baja | ✅ | Falta espacio tras “Predicción:”. |
| L7 | Baja | ✅ | Se usa `º` (ordinal) en vez de `°` (grado) y sin espacio antes de la unidad. |

---

## Detalle de cada fallo

### F — Funcionalidad, datos y UI

**F1 — AppBar bajo la barra de estado (Crítica) — ✅**
La barra superior se dibujaba debajo del reloj/wifi/batería tras recrear la actividad (cambio de tema, idioma o tamaño de fuente). Se aplicaron insets explícitos al AppBar y al scroll. *Queda verificado en claro y oscuro.*

**F2 — El idioma no cambia al momento (Crítica) — ✅**
El badge cambiaba a GL/ES pero los textos seguían en el idioma anterior hasta cerrar y abrir. Ahora el locale se aplica en `attachBaseContext` y se recrea la actividad al cambiar. *Verificado: ES→GL inmediato.*

**F3 — Carga/error con datos falsos (Alta) — ✅**
Durante la carga o ante un fallo se veían `ESTACIÓN`, `Predicción:` sin fecha, `0.0ºC` y `00%`. Ahora se usan `-- °C` / `--%`, un error persistente y Snackbar con “Reintentar”. *No incluye aún mostrar la última predicción guardada; eso es F14.*

**F4 — Iconos de viento invisibles en claro (Alta) — ✅**
Flechas blancas sobre tarjeta clara (~1.13:1). Se añadió `wind_icon_tint` (oscuro en claro, claro en oscuro).

**F5 — Contrastes insuficientes (Alta) — ✅**
Pestañas no seleccionadas, lluvia y textos secundarios no llegaban a AA. Se ajustaron colores (`tab_text_unselected`, `text_accent`, `text_secondary`) y el fondo de pestañas.

**F6 — Touch targets pequeños (Alta) — ✅**
Badge ES/GL a 48×48 dp. Los spinners interceptan el toque en todo el campo (`setOnTouchListener`) y exponen `ACTION_CLICK` por accesibilidad, así que cualquier punto del campo abre su diálogo.

**F7 — Título solapado en pantallas pequeñas (Alta) — ✅**
Se añadió `ToolbarTitle` (20 sp, 1 línea, ellipsis) y título corto “WindMill” cuando el ancho es <400 dp o la fuente >1.15.

**F8 — Bandas blancas en pestañas (Alta) — ✅**
El fondo del TabLayout no cubría todo el ancho. Se puso fondo `colorPrimaryDark` y `tabGravity="fill"`.

**F9 — Accesibilidad de cielo/viento/temperaturas (Alta) — ✅**
Se añadieron descripciones dinámicas reales (“Chubasco 75%”, “Viento del NO”, “Máxima: 19 grados”) y etiquetas visibles “Máx.”/“Mín.” bajo cada temperatura, ambas con el mismo peso visual.

**F10 — Termómetros con fondo opaco (Alta/Media) — ✅**
Se sustituyeron por vectores locales `ic_temp_max`/`ic_temp_min` con fondo transparente.

**F11 — Tipografía demasiado pequeña (Media) — ✅**
Widget a 10–13 sp. En la pantalla principal las etiquetas “Cielo/Viento/Lluvia” pasaron de 10 sp a 12 sp.

**F12 — Diálogos de ubicación pobres (Media) — ✅**
Diálogos con título (“Seleccionar Provincia/Localidad”), elemento actual marcado y buscador con normalización de acentos en el de localidades. El filtrado limpia la marca anterior (`clearChoices`) antes de re-marcar.

**F13 — El tema reinicia la pestaña (Media) — ✅**
Se guarda `currentTabPosition` y se restaura tras `recreate()`. Si hay datos en caché de la misma zona, se muestran sin relanzar la descarga.

**F14 — Error sin conexión bloqueante (Media) — ✅**
Sin red se muestra la última predicción guardada de la zona con Snackbar (“No se pudo actualizar…”) y acción Reintentar. Si no hay caché, aparece un diálogo cancelable con Reintentar / Cambiar ubicación / Cerrar.

**F15 — Datos antiguos durante la carga (Media) — ✅**
Al cambiar de localidad se limpia la cabecera a “Cargando <concello>…”, las temperaturas a `-- °C` y la lluvia a `--%`; el contenedor de datos se oculta hasta recibir la respuesta.

**F16 — Hueco bajo las temperaturas (Media) — ✅**
El `ProgressBar` y el contenedor de datos usan `GONE` al terminar/error, así que ya no queda el hueco vacío bajo las temperaturas.

**F17 — “PASADO” ambiguo (Media/Baja) — ⬜**
Puede leerse como “pasado” o “pasado mañana”. *Propuesta: “PASADO MAÑÁ”/“DÍA 3” o fechas en las pestañas.*

**F18 — Falta espacio tras “Predicción:” (Baja) — ✅**
Se usa `prediction_format` (`Predicción: %1$s`).

**F19 — Mínima en gris y sin etiquetas (Baja/Media) — ✅**
La mínima usa `text_primary` y ambas temperaturas llevan etiquetas visibles “Máx.”/“Mín.”, además de las descripciones para TalkBack.

**F20 — Última tarjeta pegada a la barra de gestos (Baja/Media) — ✅**
`clipToPadding=false` + inset inferior en el scroll.

**F21 — Widget con textos/etiquetas genéricas (Media) — ✅**
Corregido junto con W1, W4, W9 y la ciudad de ejemplo.

---

### W — Widget

**W1 — Etiqueta “MAÑANA” en la franja de la mañana (Media) — ✅**
`widget_weather.xml` usa `@string/morning`.

**W2 — Actualización bloqueada por imágenes (Alta) — ✅**
Se publican primero los textos y después los iconos (dos fases) y se añadieron timeouts de 5 s.

**W3 — El widget no sigue el modo oscuro in-app (Media) — ✅**
`applyWidgetTheme()` lee `dark_mode` y aplica colores y fondo explícitos al `RemoteViews`; verificado en claro y oscuro.

**W4 — Textos muy pequeños (Media) — ✅**
Fecha 10 sp, periodos 11 sp, lluvia 12 sp, estado 12 sp.

**W5 — Widget pequeño sin mínima (Baja) — ✅**
Ahora muestra rango máxima/mínima.

**W6 — `00%` cuando falta lluvia (Media) — ✅**
Fallback `--%`.

**W7 — `reconfigurable` sin configuración (Baja) — ✅**
Se eliminó el flag.

**W8 — Sin preview en el selector (Baja) — ✅**
Añadidos `previewLayout` y `previewImage`.

**W9 — Descripciones genéricas de cielo (Media) — ✅**
El provider asigna la condición real por periodo.

---

### A — Accesibilidad

**A1 — Toggle de idioma sin etiqueta (Alta) — ✅**
`contentDescription` “Idioma actual: X. Cambiar a Y”, 48×48 dp.

**A2 — Cielo/viento solo imagen (Alta) — ✅**
Descripciones dinámicas con condición y dirección.

**A3 — Temperaturas sin contexto (Media) — ✅**
“Máxima: 19 grados” / “Mínima: 14 grados”.

**A4 — Pestañas sin rol/posición (Media) — ✅**
“HOY, pestaña 1 de 3”, etc.

**A5 — Splash accesible y 3 s fijos (Baja) — ✅**
GIF marcado `importantForAccessibility="no"` y el retardo fijo bajó de 3000 ms a 900 ms (medido ~1,8 s hasta `MainActivity`, incluido el arranque).

**A6 — Botón de tema sin estado (Baja) — ✅**
Anuncia “Cambiar a modo oscuro/claro” según el estado.

---

### L — Responsive

**L1 — Wrap de temperatura en 320 dp (Alta) — ✅**
Pesos, autosize 20–32 sp, `maxLines=1` y espacio duro antes de `°C`.

**L2 — 320 dp + fuente 1,3 (Alta) — ✅**
Probado con título corto y sin wrap de temperatura.

**L3 — Pestañas con bandas en tablet (Media) — ✅**
`tabGravity="fill"` y fondo del contenedor.

**L4 — Título bajo la barra de estado al hacer scroll (Media) — ✅**
Insets en AppBar/scroll; el título ya no queda tapado.

**L5 — Tablet en una sola columna (Baja) — ✅**
El contenido va dentro de un `ConstraintLayout` con ancho máximo 720 dp y centrado; en tablet (1600×2560 / 320 dpi) se ve una columna centrada en vez de estirarse a todo el ancho.

**L6 — Falta espacio tras “Predicción:” (Baja) — ✅**
Incluido en F18.

**L7 — `º` en vez de `°` (Baja) — ✅**
Se usa `°` con espacio duro (`\u00A0°C`).

---

## Pendientes

- **F17** — aclarar “PASADO” (no incluido en el lote solicitado).

Todos los IDs solicitados (F6, F9, F11–F16, F19, W3, A5, L5) están corregidos y verificados en emulador.

---

## Notas

- El DNS roto del emulador no era un fallo de la app: se restauró reiniciando el emulador con `-dns-server 8.8.8.8,8.8.4.4`. No hay proxy activo.
- Los estados ✅/🟡 reflejan el código actual en el repositorio, no una release publicada.
- Si algún ID no coincide con tu lista original, dímelo y lo renombro.
