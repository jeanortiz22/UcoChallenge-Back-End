#!/usr/bin/env bash
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