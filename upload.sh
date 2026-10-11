#!/usr/bin/env bash
set -euo pipefail

cd ~/PandaMacro/PandaMacroStarter

if [ ! -s token.txt ]; then
  echo "Ошибка: файл token.txt пуст или отсутствует."
  exit 1
fi

GH_TOKEN="$(tr -d '\r\n' < token.txt)"
chmod 600 token.txt

git init
git config user.name "tooo903"
git config user.email "tooo903@users.noreply.github.com"

# Не допускаем попадания токена и служебных файлов в коммит
touch .gitignore
grep -qxF 'token.txt' .gitignore || echo 'token.txt' >> .gitignore
grep -qxF 'upload.sh' .gitignore || echo 'upload.sh' >> .gitignore

git add .
if ! git rev-parse --verify HEAD >/dev/null 2>&1; then
  git commit -m "Initial Panda Macro project"
elif ! git diff --cached --quiet; then
  git commit -m "Update Panda Macro project"
fi

git branch -M main

if git remote get-url origin >/dev/null 2>&1; then
  git remote set-url origin https://github.com/tooo903/PandaMacro-Termux.git
else
  git remote add origin https://github.com/tooo903/PandaMacro-Termux.git
fi

ASKPASS="$(mktemp)"
trap 'rm -f "$ASKPASS"; unset GH_TOKEN' EXIT

cat > "$ASKPASS" <<'ASK'
#!/bin/sh
case "$1" in
  *Username*) printf '%s\n' "tooo903" ;;
  *Password*) printf '%s\n' "$GH_TOKEN" ;;
  *) exit 1 ;;
esac
ASK

chmod 700 "$ASKPASS"
export GH_TOKEN
GIT_ASKPASS="$ASKPASS" GIT_TERMINAL_PROMPT=0 git push -u origin main

echo "Загрузка завершена: https://github.com/tooo903/PandaMacro"
