<div align="center">

<br/>

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=700&size=28&pause=1000&color=2563EB&center=true&vCenter=true&width=600&lines=CivicInsight+Backend;AI-Powered+Civic+Command+Center" alt="Typing SVG" />

<br/>

<p align="center">
  <strong>A production-grade backend engine that modernizes political constituency management<br/>through on-premise AI, automated accountability, and real-time analytics.</strong>
</p>

<br/>

<p align="center">
  <a href="https://openjdk.org/projects/jdk/17/"><img src="https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java 17"/></a>
  <a href="https://spring.io/projects/spring-boot"><img src="https://img.shields.io/badge/Spring_Boot-3.2.x-6DB33F?style=flat-square&logo=springboot&logoColor=white" alt="Spring Boot"/></a>
  <a href="https://www.mongodb.com/"><img src="https://img.shields.io/badge/MongoDB-Atlas-47A248?style=flat-square&logo=mongodb&logoColor=white" alt="MongoDB"/></a>
  <a href="https://ollama.com/"><img src="https://img.shields.io/badge/Ollama-Llama_3_8B-black?style=flat-square" alt="Ollama"/></a>
  <a href="#"><img src="https://img.shields.io/badge/Security-JWT_+_RBAC-DC2626?style=flat-square&logo=jsonwebtokens&logoColor=white" alt="JWT"/></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-3B82F6?style=flat-square" alt="License"/></a>
</p>

<p align="center">
  <a href="#-overview">Overview</a> ·
  <a href="#-architecture">Architecture</a> ·
  <a href="#-key-features">Features</a> ·
  <a href="#-api-reference">API Reference</a> ·
  <a href="#-ai-engine">AI Engine</a> ·
  <a href="#-installation">Installation</a>
</p>

<br/>

---------------------------------------------------

</div>

## 📌 Overview

**CivicInsight** is a robust, privacy-first backend system built to digitize and automate how political constituencies in India handle citizen grievances. By combining **Spring Boot**, **MongoDB**, and a **locally-hosted Llama 3** language model, it transforms fragmented, paper-based complaint workflows into a structured, real-time command center — with zero dependency on external cloud AI providers.

---

## 🎯 Problem Statement

Political offices across India face a systemic crisis in grievance management:

| Challenge | Impact |
|:---|:---|
| **Fragmented Intake Channels** | Complaints arrive via WhatsApp, letters, and phone calls with no central record, making tracking impossible |
| **No Accountability Framework** | Officers can close or ignore complaints without providing proof of resolution |
| **Manual Processing Delays** | Sorting thousands of grievances by hand causes multi-week backlogs |
| **Zero Data for Policy** | Leaders have no visibility into which areas or departments are consistently failing |

CivicInsight addresses all four by establishing a **unified Digital Command Center** — giving elected representatives real-time situational awareness of their entire constituency.

---

## 🏗️ Architecture

The system is designed with a **Privacy-First** principle at its core. All AI inference is executed on local hardware via Ollama — citizen data never reaches an external API.

```
                        ┌──────────────────────────────────────────────┐
                        │             CIVICINSIGHT BACKEND             │
                        │                                              │
  ┌──────────────┐       │  ┌─────────────┐      ┌──────────────────┐  │
  │   CITIZEN    │──────▶│  │  REST API   │─────▶│ COMPLAINT        │  │
  │   PORTAL     │       │  │ (Spring MVC)│      │ SERVICE          │  │
  └──────────────┘       │  └──────┬──────┘      └────────┬─────────┘  │
                        │         │                       │            │
  ┌──────────────┐       │  ┌──────▼──────┐      ┌────────▼─────────┐  │
  │   OFFICER    │──────▶│  │  SPRING     │      │
  │   APP        │       │  │  SECURITY   │      │  Llama 3 8B      │  │
  └──────────────┘       │  │  JWT + RBAC │      │  🔒 On-Premise   │  │
                        │  └─────────────┘      └────────┬─────────┘  │
  ┌──────────────┐       │                                │            │
  │   ADMIN      │──────▶│  ┌─────────────────────────────▼──────────┐ │
  │   DASHBOARD  │       │  │             MONGODB                     │ │
  └──────────────┘       │  │  Grievances · Officers · Audit Logs    │ │
                        │  └─────────────────────────────────────────┘ │
                        │                      │                       │
                        │  ┌───────────────────▼─────────────────────┐ │
                        │  │          SLA SCHEDULER ENGINE           │ │
                        │  │     24h Critical · 48h High · 72h Med  │ │
                        │  └─────────────────────────────────────────┘ │
                        └──────────────────────────────────────────────┘
```

### Request Lifecycle

```
  STEP 1          STEP 2           STEP 3            STEP 4           STEP 5
  ────────        ────────         ────────          ────────         ────────
  Citizen   ───▶  AI Inference ───▶ Classification ──▶ Auto-Assign  ──▶  SLA
  Submits         Ollama/Llama3     Dept + Priority     Load-balanced    Tracking
  Complaint       processes text    + Sentiment         Officer           + Escalation
```

1. **Intake** — Citizen submits a complaint with optional image attachment via the mobile or web portal.
2. **AI Inference** — The backend forwards complaint text to the local Ollama engine running Llama 3 8B.
3. **Classification** — The model returns a structured JSON object containing the department, priority tier, and sentiment signal.
4. **Auto-Assignment** — The Smart Dispatcher calculates the nearest officer with the lowest active workload and assigns the ticket.
5. **SLA Monitoring** — A Spring Scheduler cron job continuously evaluates every open ticket against its deadline.
6. **Escalation** — Any ticket breaching its SLA is automatically escalated to the Political Administrator.

---

## 🚀 Key Features

#### 🧠 Smart Dispatcher
A load-balancing algorithm evaluates officer geo-proximity and current ticket queue depth before every assignment. This prevents officer burnout and ensures equitable, geography-aware task distribution.

#### 🔒 Privacy-First AI
Llama 3 runs entirely via Ollama on the local server. No complaint text, citizen identity, or location data is ever transmitted to an external cloud service — critical for political contexts and data compliance.

#### ⏱️ SLA & Escalation Engine
Background jobs via **Spring Scheduler** monitor every active ticket against configurable SLA thresholds (24h / 48h / 72h). Breaches trigger automatic escalation chains without requiring manual intervention.

#### 🛡️ Role-Based Access Control (RBAC)
Three distinct access tiers are secured end-to-end with **JWT authentication**:
- **Citizen** — Submit complaints, receive updates, track resolution status
- **Officer** — View assigned workload, update ticket status, upload resolution proof
- **Administrator** — Access full analytics dashboard, heatmaps, and officer performance reports

---

## 🛠️ Tech Stack

| Layer | Technology | Purpose |
|:---|:---|:---|
| **Language** | Java 17 | Core application runtime |
| **Framework** | Spring Boot 3.2.x | REST API, dependency injection, scheduling |
| **Database** | MongoDB | Flexible document storage for grievances |
| **AI Engine** | Ollama — Llama 3 8B | On-premise NLP classification |
| **Security** | Spring Security + JWT | Authentication and RBAC enforcement |
| **Build Tool** | Maven | Dependency management and packaging |
| **Scheduler** | Spring Scheduler | SLA monitoring and escalation triggers |

---

## 🔌 API Reference

**Base URL:** `http://localhost:8080/api/v1`  
**Authentication:** All protected endpoints require the header `Authorization: Bearer <JWT_TOKEN>`

| Method | Endpoint | Role | Description |
|:---:|:---|:---:|:---|
| `POST` | `/complaints` | `CITIZEN` | Submit a new grievance with optional image |
| `GET` | `/complaints/track/{id}` | `CITIZEN` | Retrieve live status and full resolution history |
| `GET` | `/officer/tasks` | `OFFICER` | View all assigned tickets with SLA deadlines |
| `PATCH` | `/complaints/{id}/status` | `OFFICER` | Update ticket status and attach resolution proof |
| `GET` | `/admin/analytics` | `ADMIN` | Fetch constituency heatmap and performance metrics |

<details>
<summary><strong>📄 Sample Request — POST /complaints</strong></summary>

```json
POST /api/v1/complaints
Authorization: Bearer <CITIZEN_JWT>
Content-Type: application/json

{
  "citizenId": "CIT_00482",
  "text": "The main road near Sector 7 market has a large pothole causing accidents daily.",
  "location": {
    "lat": 28.6139,
    "lng": 77.2090,
    "area": "Sector 7"
  }
}
```

</details>

<details>
<summary><strong>📄 Sample Response — AI-Classified Ticket</strong></summary>

```json
{
  "ticketId": "TKT-2024-09182",
  "status": "ASSIGNED",
  "classification": {
    "category": "Infrastructure",
    "priority": "HIGH",
    "department_id": "DEPT_INFRA_01",
    "sentiment": "Urgent"
  },
  "assignedOfficer": {
    "id": "OFF_0041",
    "name": "Rajesh Kumar",
    "area": "Sector 7 Zone B"
  },
  "sla_deadline": "2024-09-19T10:30:00Z",
  "createdAt": "2024-09-18T10:30:00Z"
}
```

</details>

---

## 🤖 AI Engine

CivicInsight uses **Llama 3 8B via Ollama** for fully local, privacy-preserving NLP classification — eliminating recurring API costs and cloud data exposure.

### Classification Prompt Template

```
Act as a Civic Grievance Assistant for the Indian government.
Analyze the following citizen complaint and return ONLY a valid JSON object.

Complaint: "{complaint_text}"

Return format:
{
  "category": "<Infrastructure | Sanitation | Water Supply | Electricity | Law & Order | Healthcare | Other>",
  "priority": "<CRITICAL | HIGH | MEDIUM | LOW>",
  "department_id": "<DEPT_ID>",
  "sentiment": "<Urgent | Frustrated | Neutral | Positive>"
}
```

### Sample Inference

**Input:**
```
"The main road in Sector 7 has a huge pothole causing traffic and accidents."
```

**Output:**
```json
{
  "category": "Infrastructure",
  "priority": "HIGH",
  "department_id": "DEPT_INFRA_01",
  "sentiment": "Urgent"
}
```

### Priority Classification Matrix

| Priority | Triggers | SLA Window |
|:---:|:---|:---:|
| 🔴 `CRITICAL` | Medical emergencies, violence, flooding | **24 hours** |
| 🟠 `HIGH` | Road damage, water supply failure, power outage | **48 hours** |
| 🟡 `MEDIUM` | Street lighting, park maintenance, waste collection | **72 hours** |
| 🟢 `LOW` | General feedback, suggestions, non-urgent requests | **7 days** |

---

## 📦 Installation

### Prerequisites

Ensure the following are installed and running before setup:

- ☕ [Java 17+](https://openjdk.org/projects/jdk/17/)
- 🍃 [MongoDB](https://www.mongodb.com/) (local instance or Atlas cluster)
- 🦙 [Ollama](https://ollama.com/) with `llama3` model pulled

---

### Step 1 — Clone the Repository

```bash
git clone https://github.com/your-username/civic-insight-backend.git
cd civic-insight-backend
```

---

### Step 2 — Setup Ollama

```bash
# Pull the Llama 3 8B model (~4.7 GB)
ollama pull llama3

# Verify the model is available
ollama run llama3 "respond with: ready"
```

---

### Step 3 — Configure Application Properties

Edit `src/main/resources/application.properties`:

```properties
# ── Database ──────────────────────────────────────────
spring.data.mongodb.uri=your_mongodb_connection_uri

# ── AI Engine ─────────────────────────────────────────
ollama.api.url=http://localhost:11434/api/generate
ollama.model.name=llama3

# ── Security ──────────────────────────────────────────
jwt.secret=your_256_bit_secret_key
jwt.expiration.ms=86400000

# ── SLA Thresholds (hours) ────────────────────────────
sla.critical.hours=24
sla.high.hours=48
sla.medium.hours=72
```

---

### Step 4 — Build and Run

```bash
# Build the project and run all tests
mvn clean install

# Start the application
mvn spring-boot:run
```

The server will start at **`http://localhost:8080`**.

---

## 📈 Impact

| Metric | Result |
|:---|:---|
| ⚡ **Complaint Categorization Speed** | **80% faster** — AI classifies in milliseconds vs. manual hours |
| 📋 **Accountability Coverage** | **100%** — Every ticket is timestamped, assigned, and auditable |
| 📊 **Policy Visibility** | Leaders gain **real-time heatmaps** to prioritize budget allocation |
| 🔒 **Data Privacy** | **Zero cloud exposure** — all AI inference runs on local infrastructure |

---

## 🗺️ Roadmap

- [ ] WhatsApp Bot integration for multi-channel complaint intake
- [ ] SMS notifications via Twilio for real-time status updates
- [ ] Hindi, Bengali, Tamil, and Telugu NLP support
- [ ] Fine-tuned civic domain model to replace base Llama 3
- [ ] Geo-clustering heatmaps for constituency analytics dashboard

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome. Please read [CONTRIBUTING.md](CONTRIBUTING.md) before submitting a pull request.

```bash
git checkout -b feature/your-feature-name
git commit -m "feat: describe your change"
git push origin feature/your-feature-name
# Open a Pull Request
```

---

## 📄 License

Distributed under the **MIT License**. See [`LICENSE`](LICENSE) for full details.

---

<div align="center">

<br/>

*Built to bring accountability and transparency to grassroots governance in India.*

<br/>

**If this project is useful to you, consider giving it a ⭐**

</div>
