#!/usr/bin/env sh
set -eu

ARTIFACT_DIR="ai-artifacts"

if [ ! -d "${ARTIFACT_DIR}" ]; then
  echo "[AI Artifacts] No se encontró el directorio '${ARTIFACT_DIR}'." >&2
  exit 1
fi

tmp_file="$(mktemp)"
cleanup() {
  rm -f "${tmp_file}"
}
trap cleanup EXIT INT TERM

find "${ARTIFACT_DIR}" -maxdepth 1 -type f \( -name '*.md' -o -name '*.txt' \) -print > "${tmp_file}"

if [ ! -s "${tmp_file}" ]; then
  echo "[AI Artifacts] No se encontraron archivos .md o .txt en '${ARTIFACT_DIR}'." >&2
  exit 1
fi

missing_count=0
missing_output=""
valid_output=""

while IFS= read -r file; do
  if [ ! -s "${file}" ]; then
    echo "[AI Artifacts] El archivo '${file}' está vacío." >&2
    exit 1
  fi

  if grep -qi "AI Artifact" "${file}"; then
    valid_output="${valid_output} - ${file}\\n"
  else
    missing_output="${missing_output} - ${file}\\n"
    missing_count=$((missing_count + 1))
  fi
done < "${tmp_file}"

if [ "${missing_count}" -eq 0 ]; then
  echo "[AI Artifacts] Se encontraron artefactos AI válidos:"
  printf '%b' "${valid_output}"
  exit 0
fi

echo "[AI Artifacts] Los siguientes archivos no indican ser artefactos AI:" >&2
printf '%b' "${missing_output}" >&2
exit 1