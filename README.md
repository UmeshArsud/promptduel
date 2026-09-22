# PromptDuel ⚔️
*A full-stack AI prompt evaluation sandbox for structured, side-by-side prompt engineering.*

## What is PromptDuel?
PromptDuel is a full-stack web application that allows users to create **Prompt Projects**, define multiple versions of an AI system prompt, and test them concurrently against a standardized set of user inputs. It runs all combinations through the Google Gemini API, automatically scores the outputs based on strict criteria, and presents the results in an interactive, side-by-side matrix dashboard.

## The Problem It Solves
Comparing AI system prompts today is often manual, subjective, and irreproducible. Developers typically paste prompts into a chat UI, eyeball the results, and rely on "vibes" to decide which prompt is better. **PromptDuel makes prompt engineering structured, measurable, and repeatable.**

## Key Features
- 🚀 **Multi-Version Prompt Comparison:** Test an unlimited number of system prompts simultaneously.
- 🧪 **Reusable Test Inputs:** Define a standardized test suite of inputs to evaluate prompts against.
- ⚡ **Parallel Evaluation Execution:** Concurrent LLM API calls via a multi-threaded execution pool.
- 📊 **Multi-Metric Scoring:** Automated rule-based scoring (Keyword extraction, Output length).
- ⚖️ **Toggleable LLM-as-a-Judge:** Optional secondary LLM call to score qualitative responses (1-10 scale).
- 🛡️ **Per-Call Failure Isolation:** API rate limits or failures on one prompt won't crash the entire evaluation matrix.
- 🔐 **Secure JWT Authentication:** Full user registration, login, and protected project access.
- 📈 **Results Dashboard:** Beautiful, dark-themed responsive UI with winner-highlighting and deep error-inspection.

## Tech Stack
| Tier | Technology |
|---|---|
| **Frontend** | React, Vite, TailwindCSS (v3) |
| **Backend** | Java 17, Spring Boot 3 |
| **Database** | MySQL 8 |
| **AI Provider** | Google Gemini API (gemini-2.5-flash) |
| **Authentication** | JSON Web Tokens (JWT) |

## Architecture Overview
PromptDuel follows a classic **Three-Tier Architecture**:
1. **Presentation Layer (React/Vite):** A dynamic SPA handling state management, JWT persistence, and complex data visualization.
2. **Business Logic Layer (Spring Boot):** RESTful controllers delegating to service layers. Concurrency is managed via Java `CompletableFuture` thread pools.
3. **Data Layer (MySQL / Hibernate JPA):** Relational schema tracking users, projects, prompts, inputs, and individual test runs.

**OOP Patterns Used:**
- **Inheritance & MappedSuperclass:** Used in `AuditableEntity` to automatically track `createdAt` and `updatedAt` timestamps across all models.
- **Interface Abstraction:** The `LlmClient` interface abstracts the LLM provider, allowing seamless future integration of OpenAI or Anthropic.
- **Strategy Pattern:** `ScoringStrategy` interface allows dynamic composition of scoring rules (`KeywordScoringStrategy`, `LengthScoringStrategy`, `LlmJudgeScoringStrategy`).
- **Builder Pattern:** Used extensively to construct robust `LlmRequest` JSON payloads for the Gemini REST API.

## Prerequisites
- **Java 17+**
- **Node.js 18+**
- **MySQL 8**
- **Google Gemini API Key**

## Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/UmeshArsud/promptduel.git
cd promptduel
```

### 2. Database Setup
Ensure your local MySQL instance is running. You do not need to create tables; Hibernate will auto-generate them. However, you must create the database schema:
```sql
CREATE DATABASE promptduel;
```

### 3. Environment Variables
You must set the following environment variables before starting the backend (can be set via `.env` or system variables):

- `DB_URL`: The JDBC connection string (e.g., `jdbc:mysql://localhost:3306/promptduel?createDatabaseIfNotExist=true`)
- `DB_USER`: Your MySQL username.
- `DB_PASSWORD`: Your MySQL password.
- `JWT_SECRET`: A secure, random string (minimum 256-bits) used to sign user authentication tokens.
- `GEMINI_API_KEY`: Your private Google Gemini API key.
- `GEMINI_MODEL`: (Optional) Defaults to `gemini-2.5-flash`.
- `CORS_ORIGIN`: (Optional) Defaults to `http://localhost:5173`.

### 4. Running the Backend
From the `backend` directory, run:
```bash
cd backend
./mvnw clean compile spring-boot:run
```
The backend will start on `http://localhost:8080`.

### 5. Running the Frontend
From the `frontend` directory, install dependencies and start the Vite dev server:
```bash
cd frontend
npm install
npm run dev
```
The frontend will start on `http://localhost:5173`.

## API Overview
| Endpoint | Method | Description |
|---|---|---|
| `/api/auth/register` | `POST` | Register a new user |
| `/api/auth/login` | `POST` | Authenticate and retrieve JWT |
| `/api/projects` | `GET` / `POST` | List or create prompt projects |
| `/api/projects/{id}/versions` | `GET` / `POST` | Manage system prompt versions |
| `/api/projects/{id}/inputs` | `GET` / `POST` | Manage test inputs and keywords |
| `/api/projects/{id}/evaluate` | `POST` | Trigger concurrent evaluation matrix |

## Screenshots
*[Add screenshot of Dashboard here]*
*[Add screenshot of Evaluation Results Grid here]*
*[Add screenshot of Prompt Project Editor here]*

## Future Improvements
- **Multi-Provider Support:** Plug in OpenAI and Anthropic clients into the `LlmClient` interface.
- **Team Collaboration:** Share projects and evaluation results with other registered users.
- **Exportable Reports:** Download evaluation matrices as PDF or CSV.
- **Streaming LLM Outputs:** Real-time generation rendering in the evaluation UI.

## License
MIT License. See `LICENSE` for more information.
