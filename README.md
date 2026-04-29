# VaultBank API 🏦

API REST de um banco fictício feita com **Spring Boot + MySQL**, rodando em containers Docker e deployada em uma VM na Azure.

Projeto do **CP3 — DevOps & Cloud** da FIAP, turma 2TDSPW 2026.

## Integrantes

- **João Vitor Lacerda Consorte** — RM 565565

---

## O que é

Uma API bancária simples com CRUD de contas e transações. O objetivo era dockerizar a aplicação e subir numa VM na Azure, mostrando que tudo funciona em cloud.

### Tecnologias usadas

- Java 17 + Spring Boot 3.2.5
- MySQL 8.0 (container Docker)
- Docker + Docker Compose
- Azure VM (Ubuntu 24.04)

---

## Deploy na Azure ☁️

A API está rodando na Azure nesse endereço:

| Recurso | URL |
|---------|-----|
| **Swagger UI** | http://20.225.41.26:8080/swagger-ui.html |
| **API Base** | http://20.225.41.26:8080/api/ |
| **Health Check** | http://20.225.41.26:8080/actuator/health |

### Infra

- **VM:** `vm-vaultbank-cp3` (Standard_B2pls_v2 — 2 vCPU, 4GB RAM)
- **Região:** South Central US
- **Resource Group:** `vm-devops_group`
- **OS:** Ubuntu 24.04
- **Containers:** 2 (api + mysql)

---

## Arquitetura

```
┌──────────────────────────────────────────┐
│              Azure VM                     │
│                                          │
│  ┌─────────────┐    ┌─────────────────┐  │
│  │  MySQL 8.0  │◄───│  Spring Boot    │  │
│  │  porta 3306 │    │  porta 8080     │  │
│  └─────────────┘    └─────────────────┘  │
│                                          │
│         Docker Compose Network           │
└──────────────────────────────────────────┘
                     ▲
                     │ HTTP :8080
                     │
              Requisições externas
```

---

## Endpoints da API

Base URL: `http://20.225.41.26:8080`

### Contas

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/contas` | Lista todas as contas |
| GET | `/api/contas/{id}` | Busca conta por ID |
| POST | `/api/contas` | Cria nova conta |
| PUT | `/api/contas/{id}` | Atualiza conta |
| DELETE | `/api/contas/{id}` | Deleta conta |
| PATCH | `/api/contas/{id}/desativar` | Desativa conta |

### Transações

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/contas/{contaId}/transacoes` | Lista transações da conta |
| GET | `/api/contas/{contaId}/transacoes/{id}` | Busca transação por ID |
| POST | `/api/contas/{contaId}/transacoes` | Cria transação |
| DELETE | `/api/contas/{contaId}/transacoes/{id}` | Deleta transação |

### Status

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/actuator/health` | Health check |

---

## Testando os endpoints

Pode testar direto no Swagger (`/swagger-ui.html`) ou pelo terminal com curl.

### Listar todas as contas
```bash
curl http://20.225.41.26:8080/api/contas
```

### Buscar conta por ID
```bash
curl http://20.225.41.26:8080/api/contas/1
```

### Criar nova conta
```bash
curl -X POST http://20.225.41.26:8080/api/contas \
  -H "Content-Type: application/json" \
  -d '{
    "titular": "Fulano de Tal",
    "cpf": "11122233344",
    "email": "fulano@email.com",
    "tipo": "CORRENTE",
    "saldoInicial": 1000.00
  }'
```

### Atualizar conta
```bash
curl -X PUT http://20.225.41.26:8080/api/contas/1 \
  -H "Content-Type: application/json" \
  -d '{
    "titular": "João Atualizado",
    "email": "joao.novo@email.com"
  }'
```

### Deletar conta
```bash
curl -X DELETE http://20.225.41.26:8080/api/contas/1
```

### Criar transação
```bash
curl -X POST http://20.225.41.26:8080/api/contas/2/transacoes \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "DEPOSITO",
    "valor": 500.00,
    "descricao": "Deposito teste"
  }'
```

### Listar transações de uma conta
```bash
curl http://20.225.41.26:8080/api/contas/2/transacoes
```

### Health check
```bash
curl http://20.225.41.26:8080/actuator/health
```

---

## Rodando local

Se quiser rodar no seu PC, só precisa ter Docker instalado.

```bash
git clone https://github.com/joaolacerdaconsorte/CP3-CLOUD.git
cd CP3-CLOUD
docker compose up -d --build
```

Depois é só acessar: http://localhost:8080/swagger-ui.html

Para parar:
```bash
docker compose down
```

---

## Estrutura do projeto

```
CP3-CLOUD/
├── src/                          # Código fonte Java
│   └── main/
│       ├── java/.../vaultbank/
│       │   ├── controller/       # Controllers REST
│       │   ├── model/            # Entidades JPA (Conta, Transacao)
│       │   ├── repository/       # Repositórios Spring Data
│       │   ├── service/          # Regras de negócio
│       │   ├── dto/              # Objetos de request/response
│       │   ├── config/           # Configurações (Swagger, DataLoader)
│       │   └── exception/        # Tratamento de erros
│       └── resources/
│           ├── application.yml           # Config padrão
│           └── application-docker.yml    # Config pro container
├── mysql/init/                   # Script SQL de seed
├── Dockerfile                    # Build multi-stage da API
├── docker-compose.yml            # Orquestração dos containers
├── setup-vm.sh                   # Script de setup da VM
└── pom.xml                       # Dependências Maven
```

---

## Docker Compose

O projeto sobe 2 containers:

1. **vaultbank-mysql** — Banco MySQL 8.0 com dados iniciais (5 contas + 10 transações)
2. **vaultbank-api** — API Spring Boot conectada ao MySQL

Os containers estão na mesma rede Docker (`vaultbank-net`) e o MySQL tem health check configurado pra API só subir quando o banco estiver pronto.

---

## Banco de Dados

O banco é **MySQL 8.0** rodando em container Docker. Nada de H2 ou banco em memória — a aplicação conecta num MySQL real, tanto local quanto na Azure.

As tabelas são criadas automaticamente pelo Hibernate (JPA) e os dados iniciais são carregados pelo `DataLoader` na inicialização.

### Dados de seed

Na primeira execução, o sistema já cria automaticamente:
- 5 contas bancárias (corrente, poupança, investimento)
- 10 transações de exemplo (depósitos, saques, PIX, TED)
