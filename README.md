# FormPilot

FormPilot is a comprehensive, microservices-based fitness application designed to handle user management, activity tracking, and AI-driven recommendations.

The system utilizes a polyglot persistence architecture (PostgreSQL & MongoDB) and asynchronous messaging (RabbitMQ) to ensure scalability. It features a modern React frontend with Material-UI and secured access via OAuth2/OpenID Connect (Keycloak).

## 🚀 Tech Stack

### Backend

- **Language:** Java 21
- **Framework:** Spring Boot 4.0.1
- **Cloud Architecture:** Spring Cloud 2025.1.0
- **Build Tool:** Maven
- **Databases:** PostgreSQL (User), MongoDB (Activity & AI)
- **Message Broker:** RabbitMQ
- **AI Integration:** Google Gemini API

### Frontend

- **Framework:** React (Vite)
- **UI Library:** Material-UI (MUI)
- **State Management:** Redux Toolkit
- **Routing:** React Router
- **HTTP Client:** Axios (Interceptors for Auth)
- **Security:** OAuth2 PKCE (`react-oauth2-code-pkce`)

---

## 🏗 System Architecture

The backend follows a microservices architecture with the following components:

| Service              | Port   | Description                                       | DB/Tech              |
| :------------------- | :----- | :------------------------------------------------ | :------------------- |
| **Config Server**    | `8888` | Centralized configuration for all services.       | Local Classpath      |
| **Eureka Server**    | `8761` | Service Discovery Registry.                       | Netflix Eureka       |
| **API Gateway**      | `8080` | Entry point, routing, and OAuth2 Resource Server. | Spring Cloud Gateway |
| **User Service**     | `8081` | Manages user profiles.                            | PostgreSQL           |
| **Activity Service** | `8082` | Tracks fitness activities.                        | MongoDB / RabbitMQ   |
| **AI Service**       | `8083` | Generates AI recommendations via Gemini.          | MongoDB / RabbitMQ   |

### Key Infrastructure Requirements

- **RabbitMQ:** Used for the `fitness.exchange` to decouple Activity tracking from AI analysis.
- **Keycloak:** Runs on port `8181` handling Identity Management.

---

## 🛠 Prerequisites

Before running the application, ensure the following are installed and running:

1.  **Java 21 SDK**
2.  **Node.js & npm**
3.  **PostgreSQL** (Port `5432`)
4.  **MongoDB** (Port `27017`)
5.  **RabbitMQ** (Port `5672`)
6.  **Keycloak** (Port `8181`)

---

## ⚙️ Configuration & Setup

### 1. Database Setup

Create the following databases before starting services:

- **Postgres:** `formpilot_user_db`
- **MongoDB:** `formpilot_activity_db`, `formpilot_ai_db`

### 2. RabbitMQ Setup

Ensure the following exchange and queues exist (or allow Spring to auto-configure them):

- **Exchange:** `fitness.exchange`
- **Queue:** `activity.queue`
- **Routing Key:** `activity.tracking`

### 3. Environment Variables

The **AI Service** requires access to the Google Gemini API.

| Service     | Variable         | Description                      |
| :---------- | :--------------- | :------------------------------- |
| `aiservice` | `GEMINI_API_URL` | URL for the Gemini API endpoint. |
| `aiservice` | `GEMINI_API_KEY` | Your Google Gemini API Key.      |

---

## 🏃‍♂️ How to Run

### Backend Startup Sequence

To ensure service discovery and configuration loading work correctly, start the microservices in this **exact order**:

1.  **Config Server** (`configserver`)
2.  **Eureka Server** (`eureka`)
3.  _Ensure Postgres, Mongo, and RabbitMQ are running._
4.  **Core Services:**
    - User Service (`userservice`)
    - Activity Service (`activityservice`)
    - AI Service (`aiservice`)
5.  **API Gateway** (`gateway`)

### Frontend Setup

1.  Navigate to the frontend directory:
    ```bash
    cd frontend
    ```
2.  Install dependencies:
    ```bash
    npm install
    ```
3.  Start the development server:
    ```bash
    npm run dev
    ```
4.  Access the app at `http://localhost:5173` (default Vite port).

---

## 📂 Frontend Structure

- `src/main.jsx`: Entry point, Redux & AuthProvider setup.
- `src/store/`: Redux store and `authSlice` for token management.
- `src/services/api.js`: Centralized Axios instance with Auth interceptors.
- `src/authConfig.js`: OAuth2 PKCE config for Keycloak.
- `src/components/`:
  - `ActivityList.jsx`
  - `ActivityForm.jsx`
  - `ActivityDetail.jsx`

---

## 🔐 Security

- **Authentication:** The frontend uses **OAuth2 PKCE** flow to communicate with Keycloak.
- **Authorization:** The API Gateway validates JWTs via the JWK Set URI:
  - `http://localhost:8181/realms/{your-client-name}/protocol/openid-connect/certs`
