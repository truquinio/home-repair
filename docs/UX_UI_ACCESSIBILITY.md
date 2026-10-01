# UX/UI y accesibilidad

Home Repair utiliza una dirección visual de marketplace local de servicios: clara, humana, fiable y profesional. La interfaz debe priorizar comprensión, perfiles reales, valoraciones y acciones directas por encima de decoración.

## Sistema visual

- Tipografía de interfaz: Inter.
- Tipografía de marca/títulos: Paytone One.
- Color primario de la demo: `#3448D4`.
- Texto principal: `#172033`.
- Texto secundario: `#667085`.
- Fondo: `#F5F7FB`.
- Superficie: `#FFFFFF`.
- Radio principal: 22 px.
- Área de contenido: hasta 1180 px.

Los encabezados editoriales de Inicio se centran; formularios, tablas y contenido de lectura mantienen la alineación que maximiza legibilidad. Las descripciones largas no deben centrarse fuera de cards cortas.

## Accesibilidad

Objetivo de producto: WCAG 2.2 nivel AA, con algunas decisiones más amplias cuando mejoran la usabilidad.

- Navegación completa por teclado.
- Foco visible y no eliminado.
- Enlace "Saltar al contenido".
- Controles principales con objetivo práctico de 44 px.
- Labels reales en formularios, aunque visualmente se usen placeholders.
- Estados y errores no dependen sólo del color.
- Imágenes informativas tienen texto alternativo; imágenes decorativas usan `alt=""`.
- Iconos decorativos usan `aria-hidden="true"` y no sustituyen texto necesario.
- `prefers-reduced-motion` desactiva movimiento no esencial.
- Tablas incluyen caption y encabezados semánticos.
- Las rutas de la demo actualizan `document.title`, `aria-current` y foco después de navegación interna.
- El diseño debe reflow correctamente desde 375 px hasta escritorio.

## Reglas de contenido

- Español de España en toda interfaz visible.
- Verbos activos y concretos: "Guardar cambios", "Solicitar servicio", "Iniciar sesión".
- No usar emojis como iconos de interfaz.
- Iconos sociales: icono + texto visible.
- Evitar labels decorativos en mayúsculas; usar sentence case.
- Errores deben explicar qué ocurrió y cómo corregirlo.

## Validación antes de publicar

1. JavaScript sin errores de sintaxis.
2. Maven tests verdes.
3. Sin overflow horizontal a 375, 768 y 1440 px.
4. Teclado: skip link, navegación, formularios y acciones.
5. Foco visible.
6. Targets y contraste revisados.
7. Reduced motion respetado.
8. Capturas del README regeneradas si cambia la interfaz.
