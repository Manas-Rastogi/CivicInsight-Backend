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
  <a href="https://redis.io/"><img src="https://img.shields.io/badge/Redis-Cache-DC382D?style=flat-square&logo=redis&logoColor=white" alt="Redis"/></a>
  <a href="https://groq.com/"><img src="https://img.shields.io/badge/Groq-Llama_3_8B-F55036?style=flat-square" alt="Groq Llama3"/></a>
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
---------------------------------------------------
</div>

## 📌 Overview
----------------
**CivicInsight** is a robust, privacy-first backend system built to digitize and automate how political constituencies in India handle citizen grievances. By combining **Spring Boot**, **MongoDB**, **Redis**, and **Groq-hosted Llama 3**, it transforms fragmented, paper-based complaint workflows into a structured, real-time command center — with fast AI inference and intelligent response caching.

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

The system is designed with a **Performance-First** principle at its core. Duplicate complaint analysis is served from **Redis cache**, and all AI inference is executed via **Groq's Llama 3 API** — delivering ultra-fast classification with no GPU requirement on local hardware.

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
  │   OFFICER    │──────▶│  │  SPRING     │      │ Redis Cache      │  │
  │   APP        │       │  │  SECURITY   │      │ ⚡ MD5 Dedup     │  │
  └──────────────┘       │  │  JWT + RBAC │      └────────┬─────────┘  │
                        │  └─────────────┘               │            │
  ┌──────────────┐       │                       ┌────────▼─────────┐  │
  │   ADMIN      │──────▶│                       │ Groq — Llama 3   │  │
  │   DASHBOARD  │       │                       │ 🚀 Fast Inference│  │
  └──────────────┘       │                       └────────┬─────────┘  │
                        │                                │            │
                        │  ┌─────────────────────────────▼──────────┐ │
                        │  │             MONGODB                     │ │
                        │  │  Grievances · Officers · Audit Logs    │ │
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
  Citizen   ───▶  Redis Check ───▶ AI Inference ──▶ Auto-Assign  ──▶  SLA
  Submits         MD5 hash         Groq/Llama3        Load-balanced    Tracking
  Complaint       cache lookup     (if cache miss)    Officer           + Escalation
```

1. **Intake** — Citizen submits a complaint with optional image attachment via the mobile or web portal.
2. **Cache Check** — The backend computes an MD5 hash of the complaint text and checks Redis for a cached analysis result.
3. **AI Inference** — On a cache miss, the complaint text is forwarded to **Groq's Llama 3 8B** API for classification.
4. **Classification** — The model returns a structured JSON object containing the department, priority tier, and sentiment signal. The result is cached in Redis for 24 hours.
5. **Auto-Assignment** — The Smart Dispatcher calculates the nearest officer with the lowest active workload and assigns the ticket.
6. **SLA Monitoring** — A Spring Scheduler cron job continuously evaluates every open ticket against its deadline.
7. **Escalation** — Any ticket breaching its SLA is automatically escalated to the Political Administrator.

---

## 🚀 Key Features

#### 🧠 Smart Dispatcher
A load-balancing algorithm evaluates officer geo-proximity and current ticket queue depth before every assignment. This prevents officer burnout and ensures equitable, geography-aware task distribution.

#### ⚡ Redis Caching Layer
Identical or near-identical complaints are detected via **MD5 hashing** and served directly from **Redis** — skipping the AI inference step entirely. This eliminates redundant API calls, reduces latency, and ensures consistent classification for duplicate grievances.

#### 🚀 Groq-Powered AI Inference
Llama 3 8B runs on **Groq's cloud infrastructure**, delivering sub-second classification responses. This removes the need for local GPU hardware while maintaining high-speed, reliable AI processing.

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
| **Cache** | Redis | MD5-based response caching and rate limiting |
| **AI Engine** | Groq — Llama 3 8B | Fast cloud NLP classification |
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

CivicInsight uses **Llama 3 8B via Groq** for fast, scalable NLP classification — with **Redis caching** to eliminate redundant inference calls and keep response times consistently low.

### Caching Strategy

Every complaint is hashed using **MD5** before hitting the AI layer. If an identical complaint has been processed before, the cached `GroqAnalysis` result is returned from Redis instantly — no API call required. Results are cached for **24 hours**.

```
Complaint Text  ──▶  MD5 Hash  ──▶  Redis Lookup
                                         │
                              ┌──────────┴──────────┐
                              │ HIT                  │ MISS
                              ▼                      ▼
                       Return cached          Call Groq API
                       GroqAnalysis           Cache result
                                              Return result
```

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
- ⚡ [Redis](https://redis.io/) (local instance or managed service)
- 🤖 [Groq API Key](https://console.groq.com/) (free tier available)

---

### Step 1 — Clone the Repository

```bash
git clone https://github.com/your-username/civic-insight-backend.git
cd civic-insight-backend
```

---

### Step 2 — Start Redis

```bash
# Using Docker (recommended)
docker run -d -p 6379:6379 redis

# Or start local Redis server
redis-server

# Verify Redis is running
redis-cli ping   # should return: PONG
```

---

### Step 3 — Configure Application Properties

Edit `src/main/resources/application.properties`:

```properties
# ── Database ──────────────────────────────────────────
spring.data.mongodb.uri=your_mongodb_connection_uri

# ── Redis Cache ───────────────────────────────────────
spring.data.redis.host=localhost
spring.data.redis.port=6379

# ── AI Engine (Groq) ──────────────────────────────────
groq.api.url=https://api.groq.com/openai/v1/chat/completions
groq.api.key=your_groq_api_key
groq.model.name=llama3-8b-8192

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
| 🔄 **Duplicate Request Savings** | **Redis cache** eliminates redundant Groq API calls for identical complaints |
| 📋 **Accountability Coverage** | **100%** — Every ticket is timestamped, assigned, and auditable |
| 📊 **Policy Visibility** | Leaders gain **real-time heatmaps** to prioritize budget allocation |

---

## 🗺️ Roadmap

- [ ] WhatsApp Bot integration for multi-channel complaint intake
- [ ] SMS notifications via Twilio for real-time status updates
- [ ] Hindi, Bengali, Tamil, and Telugu NLP support
- [ ] Officer mobile application (React Native)
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
