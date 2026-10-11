#!/usr/bin/env bash
set -e

cd ~/PandaMacro/PandaMacroStarter

echo "=== Panda Macro: GitHub upload ==="

git init
git config user.name "tooo903"
git config user.email "tooo903@users.noreply.github.com"

git add .

if ! git rev-parse --verify HEAD >/dev/null 2>&1; then
  git commit -m "Initial Panda Macro project"
elif ! git diff --cached --quiet; then
  git commit -m "Update Panda Macro project"
fi

git branch -M main

if git remote get-url origin >/dev/null 2>&1; then
  git remote set-url origin https://github.com/tooo903/PandaMacro.git
else
  git remote add origin https://github.com/tooo903/PandaMacro.git
fi

read -rsp "Вставь GitHub Personal Access Token: " GH_TOKEN
echo
export GH_TOKEN

ASKPASS="$(mktemp)"
trap 'rm -f "$ASKPASS"; unset GH_TOKEN' EXIT

cat > "$ASKPASS" <<'ASK'
#!/bin/sh
case "$1" in
  *Username*) echo "tooo903" ;;
  *Password*) printf '%s\n' "$GH_TOKEN" ;;
  *) exit 1 ;;
esac
ASK

chmod 700 "$ASKPASS"
GIT_ASKPASS="$ASKPASS" GIT_TERMINAL_PROMPT=0 git push -u origin main

echo "Готово! Проверь https://github.com/tooo903/PandaMacro"
