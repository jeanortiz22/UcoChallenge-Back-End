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

Para más detalles operativos, consulta las plantillas en `ai-artifacts/` y el plan de capacitación en `docs/ai/enablement-plan.md`.#!/usr/bin/env bash
set -euo pipefail

ARTIFACT_DIR="ai-artifacts"

if [[ ! -d "${ARTIFACT_DIR}" ]]; then
echo "[AI Artifacts] No se encontró el directorio '${ARTIFACT_DIR}'." >&2
exit 1
fi

shopt -s nullglob
files=("${ARTIFACT_DIR}"/*.md "${ARTIFACT_DIR}"/*.txt)
shopt -u nullglob

if [[ ${#files[@]} -eq 0 ]]; then
echo "[AI Artifacts] No se encontraron archivos .md o .txt en '${ARTIFACT_DIR}'." >&2
exit 1
fi

missing_marker=()
for file in "${files[@]}"; do
if [[ ! -s "${file}" ]]; then
echo "[AI Artifacts] El archivo '${file}' está vacío." >&2
exit 1
fi
if ! grep -qi "AI Artifact" "${file}"; then
missing_marker+=("${file}")
fi
done

if [[ ${#missing_marker[@]} -eq 0 ]]; then
echo "[AI Artifacts] Se encontraron artefactos AI válidos:"
printf ' - %s\n' "${files[@]}"
exit 0
fi

echo "[AI Artifacts] Los siguientes archivos no indican ser artefactos AI: ${missing_marker[*]}" >&2
exit 1