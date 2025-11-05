# Playbook de Vibe Coding

Este playbook documenta cómo el equipo debe aplicar el enfoque **AI-First** usando Vibe Coding a lo largo del ciclo de vida de desarrollo de software.

## 1. Objetivos

- Reducir el tiempo de ideación y experimentación usando copilotos AI.
- Mantener trazabilidad clara entre prompts, respuestas y decisiones humanas.
- Garantizar que los entregables cumplan estándares de calidad antes de su despliegue.

## 2. Tipos de prompts

| Tipo de prompt | Cuándo usarlo | Buenas prácticas |
| -------------- | ------------- | ---------------- |
| **Exploratorio** | Al iniciar una tarea para entender opciones técnicas, riesgos o alternativas. | Formular la pregunta con contexto del dominio, restricciones conocidas y objetivos de negocio. Registrar el razonamiento adicional que surja de la respuesta. |
| **Generativo** | Para obtener borradores de código, documentación o scripts iniciales. | Limitar el alcance y los lenguajes. Solicitar referencias concretas (clases, endpoints, reglas). Incluir ejemplos de entrada/salida y detallar criterios de calidad. |
| **Refinamiento** | Cuando se necesita mejorar rendimiento, seguridad o legibilidad. | Compartir fragmentos específicos del repositorio, métricas y fallos observados. Pedir comparaciones contra la versión actual. |
| **Validación** | Para revisar que una solución cumpla requisitos funcionales y no funcionales. | Proporcionar casos de prueba, escenarios límite, datos de producción anonimizados y checklists de aceptación. Documentar explícitamente qué sugerencias se aprueban o rechazan. |

Todos los prompts deben almacenarse en `ai-artifacts/` usando la plantilla correspondiente y versionarse junto con el código relacionado.

## 3. Revisión humana obligatoria

1. **Revisión del prompt:** otro integrante debe validar que el prompt contenga suficiente contexto, criterios de éxito y consideraciones éticas/legales antes de ejecutar la recomendación.
2. **Revisión de la respuesta:** la persona responsable analiza la salida de la AI, destaca riesgos o supuestos y registra la decisión final en el reporte de sesión.
3. **Code Review tradicional:** además de las verificaciones automáticas, un revisor humano comprueba que los cambios resultantes cumplan con los estándares del repositorio.
4. **Registro de conformidad:** cualquier excepción o ajuste manual debe documentarse en el reporte de sesión.

## 4. Criterios de aceptación

- Existe un prompt documentado que describe la necesidad de negocio y el alcance técnico.
- El reporte de sesión incluye la respuesta seleccionada, las iteraciones y la validación humana.
- Las pruebas automatizadas relevantes pasan exitosamente.
- No se introducen vulnerabilidades de seguridad conocidas ni se degradan métricas clave.
- La documentación se actualiza con las decisiones tomadas y referencias a los artefactos AI.

## 5. Flujo recomendado

1. Identificar el objetivo y completar un prompt exploratorio.
2. Generar una propuesta inicial con prompts generativos y registrarla.
3. Iterar mediante prompts de refinamiento hasta cumplir los criterios de aceptación.
4. Ejecutar prompts de validación con casos concretos y documentar los resultados.
5. Adjuntar en la PR el vínculo a los artefactos creados y marcar los checklists requeridos.

## 6. Custodia y auditoría

- Los artefactos deben mantenerse por al menos 12 meses y asociarse al número de ticket o PR.
- Cualquier información sensible debe anonimizarse antes de ser utilizada en prompts.
- Las auditorías semestrales verificarán la existencia de artefactos y la trazabilidad de decisiones.

Para más detalles operativos, consulta las plantillas en `ai-artifacts/` y el plan de capacitación en `docs/ai/enablement-plan.md`.

## 7. ¿Cómo ensayar el flujo AI-First?

Sigue estos pasos cada vez que quieras comprobar que la práctica funciona de punta a punta antes de enviar una PR:

1. **Prepara los artefactos:** copia las plantillas desde `ai-artifacts/` y complétalas con la sesión que quieras validar. Asegúrate de mantener la marca `AI Artifact` en el encabezado.
2. **Ejecuta la verificación local:** desde la raíz del repositorio corre `./scripts/check-ai-artifacts.sh`. El script saldrá con código `0` cuando detecte al menos un artefacto válido y mostrará la lista de archivos evaluados.
   ```bash
   ./scripts/check-ai-artifacts.sh
   # [AI Artifacts] Se encontraron artefactos AI válidos:
   #  - ai-artifacts/prompt-template.md
   #  - ai-artifacts/session-report-template.md
   ```
3. **Revisa la salida:** si el script falla, revisa que los archivos no estén vacíos y que conserven la marca `AI Artifact`. Corrige y vuelve a ejecutar hasta ver el mensaje _"[AI Artifacts] Se encontraron artefactos AI válidos"_.
4. **Integra con otras pruebas:** ejecuta las pruebas unitarias o de servicio habituales del módulo que estás modificando para complementar la verificación de artefactos.
5. **Adjunta la evidencia en la PR:** enlaza los artefactos y pega el resultado del script (o de la ejecución en CI) en la sección de evidencia AI-First de la plantilla de PR.

Estos pasos replican exactamente lo que valida el flujo de CI (`scripts/check-ai-artifacts.sh`), de modo que si pasan localmente también pasarán en el pipeline.

### ¿Cómo verificarlo en GitHub Actions?

1. Abre la pestaña **Actions** del repositorio y selecciona el workflow **CI** más reciente.
2. Dentro del job correspondiente a tu módulo (por ejemplo, `Build & Test (messages-service)`), localiza el paso **Validar artefactos AI**.
3. Haz clic en el paso para ver el log. Deberías observar la misma salida que cuando lo ejecutas localmente. Si aparece un error, revisa los artefactos listados y corrige los archivos marcados.
4. Una vez que el paso se muestre en verde, el flujo de Vibe Coding se considera validado para esa ejecución.