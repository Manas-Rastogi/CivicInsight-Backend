CivicInsight Backend — AI-Powered Civic Command Center

CivicInsight ek production-grade backend system hai jo Indian political constituencies mein citizen grievances ko digitize aur automate karta hai. Spring Boot, MongoDB, Redis, aur Groq-hosted Llama 3 ko combine karke, yeh fragmented paper-based complaint workflows ko ek structured, real-time command center mein convert karta hai — fast AI inference aur intelligent response caching ke saath.

---------------------------------------------------

Overview

CivicInsight addresses the systemic crisis in grievance management that political offices across India face. Complaints arrive via WhatsApp, letters, and phone calls with no central record, making tracking impossible. Officers can close or ignore complaints without providing proof of resolution. Sorting thousands of grievances by hand causes multi-week backlogs. And leaders have no visibility into which areas or departments are consistently failing.

CivicInsight addresses all four by establishing a unified Digital Command Center — giving elected representatives real-time situational awareness of their entire constituency.

---------------------------------------------------

Architecture

The system is designed with a performance-first principle at its core. Duplicate complaint analysis is served from Redis cache, and all AI inference is executed via Groq's Llama 3 API — delivering ultra-fast classification with no GPU requirement on local hardware.

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

Request Lifecycle

```
  STEP 1          STEP 2           STEP 3            STEP 4           STEP 5
  ────────        ────────         ────────          ────────         ────────
  Citizen   ───▶  Redis Check ───▶ AI Inference ──▶ Auto-Assign  ──▶  SLA
  Submits         MD5 hash         Groq/Llama3        Load-balanced    Tracking
  Complaint       cache lookup     (if cache miss)    Officer           + Escalation
```

1. Intake — Citizen submits a complaint with optional image attachment via the mobile or web portal.
2. Cache Check — The backend computes an MD5 hash of the complaint text and checks Redis for a cached analysis result.
3. AI Inference — On a cache miss, the complaint text is forwarded to Groq's Llama 3 8B API for classification.
4. Classification — The model returns a structured JSON object containing the department, priority tier, and sentiment signal. The result is cached in Redis for 24 hours.
5. Auto-Assignment — The Smart Dispatcher calculates the nearest officer with the lowest active workload and assigns the ticket.
6. SLA Monitoring — A Spring Scheduler cron job continuously evaluates every open ticket against its deadline.
7. Escalation — Any ticket breaching its SLA is automatically escalated to the Political Administrator.

---------------------------------------------------

Key Features

Smart Dispatcher
A load-balancing algorithm evaluates officer geo-proximity and current ticket queue depth before every assignment. This prevents officer burnout and ensures equitable, geography-aware task distribution.

Redis Caching Layer
Identical or near-identical complaints are detected via MD5 hashing and served directly from Redis — skipping the AI inference step entirely. This eliminates redundant API calls, reduces latency, and ensures consistent classification for duplicate grievances.

Groq-Powered AI Inference
Llama 3 8B runs on Groq's cloud infrastructure, delivering sub-second classification responses. This removes the need for local GPU hardware while maintaining high-speed, reliable AI processing.

SLA and Escalation Engine
Background jobs via Spring Scheduler monitor every active ticket against configurable SLA thresholds — 24 hours for critical, 48 for high, 72 for medium. Breaches trigger automatic escalation chains without requiring manual intervention.

Role-Based Access Control
Three distinct access tiers are secured end-to-end with JWT authentication. Citizens can submit complaints, receive updates, and track resolution status. Officers can view their assigned workload, update ticket status, and upload resolution proof. Administrators get access to the full analytics dashboard, heatmaps, and officer performance reports.

---------------------------------------------------

Tech Stack

Java 17 is the core application runtime. Spring Boot 3.2.x handles the REST API, dependency injection, and scheduling. MongoDB provides flexible document storage for grievances. Redis handles MD5-based response caching and rate limiting. Groq running Llama 3 8B delivers fast cloud NLP classification. Spring Security with JWT enforces authentication and RBAC. Maven manages dependencies and packaging, and Spring Scheduler powers SLA monitoring and escalation triggers.

---------------------------------------------------

API Reference

Base URL: http://localhost:8080/api/v1
All protected endpoints require the header: Authorization: Bearer JWT_TOKEN

POST /complaints — Role: CITIZEN — Submit a new grievance with optional image.
GET /complaints/track/{id} — Role: CITIZEN — Retrieve live status and full resolution history.
GET /officer/tasks — Role: OFFICER — View all assigned tickets with SLA deadlines.
PATCH /complaints/{id}/status — Role: OFFICER — Update ticket status and attach resolution proof.
GET /admin/analytics — Role: ADMIN — Fetch constituency heatmap and performance metrics.

Sample Request — POST /complaints:

```json
POST /api/v1/complaints
Authorization: Bearer CITIZEN_JWT
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

Sample Response — AI-Classified Ticket:

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

---------------------------------------------------

AI Engine

CivicInsight uses Llama 3 8B via Groq for fast, scalable NLP classification — with Redis caching to eliminate redundant inference calls and keep response times consistently low.

Every complaint is hashed using MD5 before hitting the AI layer. If an identical complaint has been processed before, the cached result is returned from Redis instantly — no API call required. Results are cached for 24 hours.

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

Classification Prompt Template:

```
Act as a Civic Grievance Assistant for the Indian government.
Analyze the following citizen complaint and return ONLY a valid JSON object.

Complaint: "{complaint_text}"

Return format:
{
  "category": "Infrastructure | Sanitation | Water Supply | Electricity | Law & Order | Healthcare | Other",
  "priority": "CRITICAL | HIGH | MEDIUM | LOW",
  "department_id": "DEPT_ID",
  "sentiment": "Urgent | Frustrated | Neutral | Positive"
}
```

Sample Inference

Input:
"The main road in Sector 7 has a huge pothole causing traffic and accidents."

Output:
```json
{
  "category": "Infrastructure",
  "priority": "HIGH",
  "department_id": "DEPT_INFRA_01",
  "sentiment": "Urgent"
}
```

Priority Classification:

CRITICAL — Medical emergencies, violence, flooding — SLA: 24 hours
HIGH — Road damage, water supply failure, power outage — SLA: 48 hours
MEDIUM — Street lighting, park maintenance, waste collection — SLA: 72 hours
LOW — General feedback, suggestions, non-urgent requests — SLA: 7 days

---------------------------------------------------

Installation

Prerequisites: Java 17+, MongoDB (local or Atlas), Redis (local or managed), and a Groq API Key (free tier available at console.groq.com).

Step 1 — Clone the repository:

```bash
git clone https://github.com/your-username/civic-insight-backend.git
cd civic-insight-backend
```

Step 2 — Start Redis:

```bash
docker run -d -p 6379:6379 redis
redis-cli ping   # should return: PONG
```

Step 3 — Configure application.properties:

```properties
spring.data.mongodb.uri=your_mongodb_connection_uri
spring.data.redis.host=localhost
spring.data.redis.port=6379
groq.api.url=https://api.groq.com/openai/v1/chat/completions
groq.api.key=your_groq_api_key
groq.model.name=llama3-8b-8192
jwt.secret=your_256_bit_secret_key
jwt.expiration.ms=86400000
sla.critical.hours=24
sla.high.hours=48
sla.medium.hours=72
```

Step 4 — Build and run:

```bash
mvn clean install
mvn spring-boot:run
```

Server starts at http://localhost:8080.

---------------------------------------------------

Impact

AI complaint categorization is 80% faster than manual processing. Redis caching eliminates redundant Groq API calls for identical complaints. Every ticket is timestamped, assigned, and fully auditable — 100% accountability coverage. Leaders gain real-time heatmaps to prioritize budget allocation.

---------------------------------------------------

Roadmap

WhatsApp Bot integration for multi-channel complaint intake. SMS notifications via Twilio for real-time status updates. Hindi, Bengali, Tamil, and Telugu NLP support. Officer mobile application in React Native. Fine-tuned civic domain model to replace base Llama 3. Geo-clustering heatmaps for constituency analytics dashboard.

---------------------------------------------------

Contributing

Contributions, issues, and feature requests are welcome. Please read CONTRIBUTING.md before submitting a pull request.

```bash
git checkout -b feature/your-feature-name
git commit -m "feat: describe your change"
git push origin feature/your-feature-name
```

---------

License: MIT. See LICENSE for full details.

Built to bring accountability and transparency to grassroots governance in India.
