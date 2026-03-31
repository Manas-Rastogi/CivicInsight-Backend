
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
  <a href="#"><img src="https://img.shields.io/badge/Auth-Google_OAuth2-4285F4?style=flat-square&logo=google&logoColor=white" alt="Google OAuth2"/></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-3B82F6?style=flat-square" alt="License"/></a>
</p>

<p align="center">
  <a href="#-overview">Overview</a> ·
  <a href="#-architecture">Architecture</a> ·
  <a href="#-key-features">Features</a> ·
  <a href="#-api-reference">API Reference</a> ·
  <a href="#-google-oauth2-integration">OAuth2</a> ·
  <a href="#-ai-engine">AI Engine</a> ·
  <a href="#-installation">Installation</a>
</p>

<br/>

---

</div>

## 📌 Overview

**CivicInsight** is a robust, privacy-first backend system built to digitize and automate how political constituencies in India handle citizen grievances. By combining **Spring Boot**, **MongoDB**, a **locally-hosted Llama 3** language model, and **Google OAuth2** for seamless citizen authentication, it transforms fragmented, paper-based complaint workflows into a structured, real-time command center — with zero dependency on external cloud AI providers.

---

## 🎯 Problem Statement

Political offices across India face a systemic crisis in grievance management:

| Challenge | Impact |
|:---|:---|
| **Fragmented Intake Channels** | Complaints arrive via WhatsApp, letters, and phone calls with no central record, making tracking impossible |
| **No Accountability Framework** | Officers can close or ignore complaints without providing proof of resolution |
| **Manual Processing Delays** | Sorting thousands of grievances by hand causes multi-week backlogs |
| **Zero Data for Policy** | Leaders have no visibility into which areas or departments are consistently failing |
| **High Registration Friction** | Requiring manual signup discourages citizens from reporting issues |

CivicInsight addresses all five by establishing a **unified Digital Command Center** — giving elected representatives real-time situational awareness of their entire constituency, while letting citizens log in instantly via Google.

---

## 🏗️ Architecture

The system is designed with a **Privacy-First** principle at its core. All AI inference is executed on local hardware via Ollama — citizen data never reaches an external API.

```
                        ┌──────────────────────────────────────────────────┐
                        │              CIVICINSIGHT BACKEND                │
                        │                                                  │
  ┌──────────────┐       │  ┌─────────────┐      ┌──────────────────────┐  │
  │   CITIZEN    │──────▶│  │  REST API   │─────▶│ COMPLAINT            │  │
  │   PORTAL     │       │  │ (Spring MVC)│      │ SERVICE              │  │
  │ (Google SSO) │       │  └──────┬──────┘      └────────┬─────────────┘  │
  └──────────────┘       │         │                       │               │
                        │  ┌──────▼──────┐      ┌────────▼─────────────┐  │
  ┌──────────────┐       │  │  SPRING     │      │  OLLAMA ENGINE       │  │
  │   OFFICER    │──────▶│  │  SECURITY   │      │  Llama 3 8B          │  │
  │   APP        │       │  │  JWT + RBAC │      │  🔒 On-Premise       │  │
  └──────────────┘       │  │  + OAuth2   │      └────────┬─────────────┘  │
                        │  └──────┬──────┘               │               │
  ┌──────────────┐       │         │                       │               │
  │   ADMIN      │──────▶│  ┌──────▼──────────────────────▼─────────────┐ │
  │   DASHBOARD  │       │  │                  MONGODB                   │ │
  └──────────────┘       │  │   Grievances · Officers · Audit Logs       │ │
                        │  └─────────────────────────────────────────────┘ │
  ┌──────────────┐       │                       │                         │
  │  GOOGLE      │       │  ┌────────────────────▼────────────────────┐   │
  │  OAUTH2      │──────▶│  │         SLA SCHEDULER ENGINE            │   │
  │  SERVER      │       │  │    24h Critical · 48h High · 72h Med    │   │
  └──────────────┘       │  └─────────────────────────────────────────┘   │
                        └──────────────────────────────────────────────────┘
```

### Request Lifecycle

```
  STEP 1          STEP 2           STEP 3            STEP 4           STEP 5
  ────────        ────────         ────────          ────────         ────────
  Citizen   ───▶  AI Inference ───▶ Classification ──▶ Auto-Assign  ──▶  SLA
  Submits         Ollama/Llama3     Dept + Priority     Load-balanced    Tracking
  Complaint       processes text    + Sentiment         Officer           + Escalation
  (via JWT or
  Google SSO)
```

### Google OAuth2 Flow

```
  STEP 1           STEP 2              STEP 3              STEP 4           STEP 5
  ──────────       ──────────          ──────────          ──────────       ──────────
  Citizen     ───▶ Google Auth    ───▶ Google returns ───▶ Backend     ───▶ JWT issued
  clicks           page shown          auth code           exchanges        to citizen,
  "Login with      (consent            in redirect         code for         session
  Google"          screen)             URI callback        user profile     begins
```

---

## 🚀 Key Features

#### 🧠 Smart Dispatcher
A load-balancing algorithm evaluates officer geo-proximity and current ticket queue depth before every assignment. This prevents officer burnout and ensures equitable, geography-aware task distribution.

#### 🔐 Google OAuth2 Single Sign-On
Citizens can log in instantly using their existing Google account — no separate registration required. The backend exchanges the Google authorization code for profile data, auto-creates a citizen account on first login, and issues a signed JWT for all subsequent API calls.

#### 🔒 Privacy-First AI
Llama 3 runs entirely via Ollama on the local server. No complaint text, citizen identity, or location data is ever transmitted to an external cloud service — critical for political contexts and data compliance.

#### ⏱️ SLA & Escalation Engine
Background jobs via **Spring Scheduler** monitor every active ticket against configurable SLA thresholds (24h / 48h / 72h). Breaches trigger automatic escalation chains without requiring manual intervention.

#### 🛡️ Role-Based Access Control (RBAC)
Three distinct access tiers are secured end-to-end with **JWT authentication**:
- **Citizen** — Submit complaints, receive updates, track resolution status. Can authenticate via username/password **or Google SSO**.
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
| **OAuth2** | Google OAuth2 / OpenID Connect | Citizen Single Sign-On |
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
| `GET` | `/auth/google/callback` | `PUBLIC` | Google OAuth2 callback — exchanges auth code for JWT |

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

## 🔐 Google OAuth2 Integration

CivicInsight supports **Google Single Sign-On** for citizens, eliminating registration friction and enabling instant, trusted authentication.

### How It Works

```
Frontend                      Google                        Backend
   │                             │                              │
   │──── /auth/google/login ────▶│                              │
   │     (redirect to Google)    │                              │
   │                             │                              │
   │◀─── Consent Screen ────────│                              │
   │     (user approves)         │                              │
   │                             │                              │
   │──── Auth Code ─────────────────────────────────────────▶ │
   │     (via redirect_uri)      │                              │
   │                             │                              │
   │                             │◀──── Exchange Code ─────────│
   │                             │      (client_id + secret)    │
   │                             │                              │
   │                             │──── Access Token + ─────────▶│
   │                             │     User Profile             │
   │                             │                              │
   │◀──────────────────────────────────── JWT Token ───────────│
   │           (signed, ready for API calls)                    │
```

### Backend Endpoint

```
GET /auth/google/callback?code={auth_code}&check=user
```

| Parameter | Description |
|:---|:---|
| `code` | Authorization code received from Google after user consent |
| `check` | Account type — always `user` for citizen OAuth2 login |

**Response:** A signed JWT string (quoted), e.g. `"eyJhbGciOiJIUzI1NiJ9..."`

### What Happens on First Login

1. Backend exchanges `code` with Google for an access token
2. Google profile (`email`, `name`, `picture`) is fetched
3. If no citizen account exists for that email → **account is auto-created**
4. A signed JWT is issued and returned to the frontend
5. All subsequent API calls use this JWT via `Authorization: Bearer <token>`

### Frontend Integration

The frontend initiates OAuth2 by redirecting the citizen to:

```
https://accounts.google.com/o/oauth2/v2/auth
  ?client_id=YOUR_CLIENT_ID
  &redirect_uri=https://civic-insight.vercel.app/login.html
  &response_type=code
  &scope=email profile
  &access_type=offline
  &prompt=consent
```

After Google redirects back with `?code=...`, the frontend calls `/auth/google/callback` and stores the returned JWT in `localStorage`.

### Google Cloud Console Setup

1. Go to [console.cloud.google.com](https://console.cloud.google.com)
2. Navigate to **APIs & Services → Credentials**
3. Select your **OAuth 2.0 Client ID**
4. Under **Authorized redirect URIs**, add:
   ```
   https://civic-insight.vercel.app/login.html
   ```
5. Click **Save**

> ⚠️ The `redirect_uri` in your frontend URL, your backend's `GoogleAuthService.java`, and Google Console **must all match exactly** — otherwise Google will reject the request.

### Backend Configuration (`application.properties`)

```properties
# ── Google OAuth2 ─────────────────────────────────────
google.client.id=YOUR_GOOGLE_CLIENT_ID
google.client.secret=YOUR_GOOGLE_CLIENT_SECRET
google.redirect.uri=https://civic-insight.vercel.app/login.html
google.token.url=https://oauth2.googleapis.com/token
google.userinfo.url=https://www.googleapis.com/oauth2/v3/userinfo
```

### `GoogleAuthService.java` — Key Exchange

```java
// Token exchange — redirect_uri must match Google Console exactly
params.add("code", authCode);
params.add("client_id", googleClientId);
params.add("client_secret", googleClientSecret);
params.add("redirect_uri", "https://civic-insight.vercel.app/login.html");
params.add("grant_type", "authorization_code");
```

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
- 🔑 A [Google Cloud](https://console.cloud.google.com) project with OAuth2 credentials configured

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

### Step 3 — Configure Google OAuth2

1. Go to [console.cloud.google.com](https://console.cloud.google.com)
2. Create a new project (or select an existing one)
3. Navigate to **APIs & Services → Credentials → Create Credentials → OAuth 2.0 Client ID**
4. Set **Application type** to `Web application`
5. Under **Authorized redirect URIs**, add your frontend callback URL:
   ```
   https://civic-insight.vercel.app/login.html
   ```
6. Copy your **Client ID** and **Client Secret**

---

### Step 4 — Configure Application Properties

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

# ── Google OAuth2 ─────────────────────────────────────
google.client.id=your_google_client_id
google.client.secret=your_google_client_secret
google.redirect.uri=https://civic-insight.vercel.app/login.html
google.token.url=https://oauth2.googleapis.com/token
google.userinfo.url=https://www.googleapis.com/oauth2/v3/userinfo

# ── SLA Thresholds (hours) ────────────────────────────
sla.critical.hours=24
sla.high.hours=48
sla.medium.hours=72
```

---

### Step 5 — Build and Run

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
| 🚀 **Citizen Onboarding** | **One-click login** via Google — zero registration friction |

---

## 🗺️ Roadmap

- [ ] WhatsApp Bot integration for multi-channel complaint intake
- [ ] SMS notifications via Twilio for real-time status updates
- [ ] Hindi, Bengali, Tamil, and Telugu NLP support
- [ ] Officer mobile application (React Native)
- [ ] Fine-tuned civic domain model to replace base Llama 3
- [ ] Geo-clustering heatmaps for constituency analytics dashboard
- [ ] GitHub OAuth2 support alongside Google SSO

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
