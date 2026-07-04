# 🧪 Unit Test Generator

Automated JUnit Test Case Generator - A full-stack web application that generates comprehensive unit tests for Java classes using AST analysis.

## ✨ Features

- 📁 **File Upload** - Upload `.java` files directly
- 📝 **Code Editor** - Write or paste Java code with syntax highlighting
- 🎯 **Smart Test Generation** - Creates Normal, Edge Case, and Negative tests
- 📊 **Test Case Breakdown** - Visual overview of generated tests
- 💾 **Download Results** - Export generated test classes
- 🎨 **Modern Dark UI** - Smooth animations and glassmorphism design

## 🛠 Tech Stack

| Frontend | Backend |
|----------|---------|
| React 18 | Spring Boot 3 |
| PrismJS (syntax highlight) | JavaParser (AST analysis) |
| Lucide Icons | JUnit 5 |

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Node.js 18+
- Maven

### 1. Backend
```bash
cd backend
./mvnw spring-boot:run
# Server starts on http://localhost:8080
```

### 2. Frontend
```bash
cd frontend
npm install
npm start
# App opens on http://localhost:3000
```

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/generate` | Generate test cases from code |
| POST | `/api/upload` | Parse uploaded Java file |
| GET | `/api/health` | Health check |

## 🌐 Deployment

### Backend (Railway/Render)
```bash
./mvnw clean package
# Deploy target/unit-test-generator-1.0.0.jar
```

### Frontend (Vercel)
```bash
cd frontend
npm run build
# Deploy 'build' folder to Vercel
```

## 📋 Project Structure

```
unit-test-generator/
├── backend/          # Spring Boot application
│   ├── src/main/java/com/unittestgenerator/
│   │   ├── controller/    # REST API controllers
│   │   ├── service/       # Business logic
│   │   ├── dto/           # Data transfer objects
│   │   └── config/        # CORS & app config
│   └── pom.xml
├── frontend/         # React application
│   ├── src/components/  # UI components
│   └── package.json
└── README.md
```

## 🎓 College Submission Checklist

- [ ] GitHub Repository created
- [ ] Backend deployed (Railway/Render)
- [ ] Frontend deployed (Vercel)
- [ ] Demo video recorded (2-3 min)
- [ ] Screenshots added to docs/
- [ ] Project report prepared

## 👨‍💻 Authors

- Your Name - College Project 2024
