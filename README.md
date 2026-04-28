# 🏦 VaultBank API — CP2 DevOps

> **API REST bancária fictícia** desenvolvida para o CP2 de DevOps, composta por uma API em **Spring Boot 3.2**, banco de dados **MySQL 8.0** e containers **Docker**, pronta para deploy em **Oracle OCI / Azure / Google Cloud**.

**Aluno:** João Vitor Lacerda Consorte  
**Turma:** 2TDSPW — FIAP 2026  

---

## 📐 Arquitetura

```
┌──────────────────────────────────────────────────────┐
│                    Cloud VM (OCI/Azure/GCP)           │
│                                                      │
│  ┌──────────────────┐    ┌──────────────────────┐   │
│  │  Container MySQL  │◄──►│  Container Spring Boot│   │
│  │    (porta 3306)   │    │    (porta 8080)       │   │
│  │                   │    │                       │   │
│  │  • vaultbank DB   │    │  • REST API CRUD      │   │
│  │  • Dados persist. │    │  • Swagger UI          │   │
│  │  • UTF-8          │    │  • Health Checks       │   │
│  └──────────────────┘    └──────────────────────┘   │
│           Docker Compose Network                     │
└──────────────────────────────────────────────────────┘
```

---

## 🚀 Tecnologias

| Camada          | Tecnologia             |
|-----------------|------------------------|
| **Backend**     | Java 17 + Spring Boot 3.2.5 |
| **ORM**         | Spring Data JPA + Hibernate |
| **Banco de Dados** | MySQL 8.0 (container) |
| **Documentação** | SpringDoc OpenAPI (Swagger) |
| **Containers**  | Docker + Docker Compose |
| **Build**       | Maven 3.9 (multi-stage) |
| **Cloud**       | Oracle OCI / Azure / GCP |

---

## 📋 Endpoints da API

### 🏦 Contas (`/api/contas`)

| Método  | Endpoint                   | Descrição                          |
|---------|----------------------------|------------------------------------|
| `GET`   | `/api/contas`              | Listar todas as contas             |
| `GET`   | `/api/contas/{id}`         | Buscar conta por ID                |
| `GET`   | `/api/contas/buscar?nome=` | Buscar por nome do titular         |
| `GET`   | `/api/contas/tipo/{tipo}`  | Filtrar por tipo de conta          |
| `POST`  | `/api/contas`              | Criar nova conta                   |
| `PUT`   | `/api/contas/{id}`         | Atualizar dados da conta           |
| `PATCH` | `/api/contas/{id}/desativar` | Desativar conta (soft delete)    |
| `DELETE`| `/api/contas/{id}`         | Deletar conta permanentemente      |

### 💳 Transações (`/api/contas/{contaId}/transacoes`)

| Método  | Endpoint                                  | Descrição                   |
|---------|-------------------------------------------|-----------------------------|
| `GET`   | `/api/contas/{contaId}/transacoes`        | Listar transações da conta  |
| `GET`   | `/api/contas/{contaId}/transacoes/{id}`   | Buscar transação por ID     |
| `POST`  | `/api/contas/{contaId}/transacoes`        | Criar nova transação        |
| `DELETE`| `/api/contas/{contaId}/transacoes/{id}`   | Deletar transação           |

### Tipos de Transação
`DEPOSITO` | `SAQUE` | `TRANSFERENCIA` | `PIX` | `PAGAMENTO`

### Tipos de Conta
`CORRENTE` | `POUPANCA` | `INVESTIMENTO`

---

## 🐳 Como Executar

### Opção 1: Docker Compose (Recomendado)

```bash
# Clonar o repositório
git clone <repo-url>
cd CP3-CLOUD

# Subir os dois containers
docker compose up -d --build

# Verificar status
docker compose ps

# Ver logs
docker compose logs -f api
```

**Acesse:**
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 Console: Desabilitado no modo Docker

### Opção 2: Standalone (sem Docker)

```bash
# Compilar
mvn clean package -DskipTests

# Executar com H2 (banco em memória)
java -jar target/vaultbank-1.0.0.jar

# Ou com MySQL local
java -jar target/vaultbank-1.0.0.jar --spring.profiles.active=docker
```

---

## ☁️ Deploy em VM Cloud

### 1. Criar VM (Oracle OCI / Azure)

- **OCI:** Compute → Create Instance → Ubuntu 22.04 → Always Free
- **Azure:** Virtual Machine → Ubuntu 22.04 → Standard B1s

### 2. Configurar a VM

```bash
# Conectar via SSH
ssh -i <chave.pem> ubuntu@<IP-DA-VM>

# Executar script de setup
chmod +x setup-vm.sh
./setup-vm.sh

# Logout e login para aplicar grupo docker
exit
ssh -i <chave.pem> ubuntu@<IP-DA-VM>
```

### 3. Deploy da Aplicação

```bash
git clone <repo-url>
cd CP3-CLOUD
docker compose up -d --build
```

### 4. Liberar Portas

- **OCI:** Security List → Ingress Rules → Porta 8080
- **Azure:** NSG → Inbound Rules → Porta 8080

---

## 📝 Exemplos de Uso

### Criar conta

```bash
curl -X POST http://localhost:8080/api/contas \
  -H "Content-Type: application/json" \
  -d '{
    "titular": "Novo Aluno FIAP",
    "cpf": "44455566677",
    "email": "aluno@fiap.com.br",
    "tipo": "CORRENTE",
    "saldoInicial": 1000.00
  }'
```

### Fazer depósito

```bash
curl -X POST http://localhost:8080/api/contas/1/transacoes \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "DEPOSITO",
    "valor": 5000.00,
    "descricao": "Depósito inicial"
  }'
```

### Fazer PIX

```bash
curl -X POST http://localhost:8080/api/contas/1/transacoes \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "PIX",
    "valor": 150.00,
    "descricao": "PIX para restaurante"
  }'
```

---

## 🗃️ Dados Iniciais

A aplicação já vem com **5 contas** e **10 transações** pré-cadastradas para demonstração:

| # | Titular                        | Tipo         | Saldo        | Status |
|---|--------------------------------|-------------|--------------|--------|
| 1 | João Vitor Lacerda Consorte    | CORRENTE    | R$ 15.750,00 | Ativa  |
| 2 | Maria Silva                    | POUPANCA    | R$ 42.300,50 | Ativa  |
| 3 | Carlos Oliveira                | INVESTIMENTO| R$ 125.000,00| Ativa  |
| 4 | Ana Beatriz Ferreira           | CORRENTE    | R$ 0,00      | Inativa|
| 5 | Pedro Santos                   | CORRENTE    | R$ 8.920,75  | Ativa  |

---

## 📄 Licença

Projeto acadêmico — FIAP 2TDSPW 2026.
