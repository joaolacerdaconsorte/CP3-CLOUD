#!/bin/bash
# ========================================
# VaultBank — Setup de VM para Oracle OCI / Azure
# Aluno: João Vitor Lacerda Consorte
# FIAP — 2TDSPW 2026 — CP2 DevOps
# ========================================

set -e

echo "🔧 Atualizando sistema..."
sudo apt-get update -y && sudo apt-get upgrade -y

echo "🐳 Instalando Docker..."
sudo apt-get install -y ca-certificates curl gnupg lsb-release

sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update -y
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

echo "👤 Adicionando usuário ao grupo docker..."
sudo usermod -aG docker $USER

echo "🔥 Abrindo portas no firewall..."
sudo iptables -I INPUT 6 -m state --state NEW -p tcp --dport 8080 -j ACCEPT
sudo iptables -I INPUT 6 -m state --state NEW -p tcp --dport 3306 -j ACCEPT
sudo netfilter-persistent save 2>/dev/null || true

echo "✅ Setup concluído! Faça logout e login novamente para usar Docker sem sudo."
echo ""
echo "📋 Próximos passos:"
echo "  1. git clone <repo-url>"
echo "  2. cd <repo-dir>"
echo "  3. docker compose up -d --build"
echo "  4. Acesse: http://<IP-DA-VM>:8080/swagger-ui.html"
