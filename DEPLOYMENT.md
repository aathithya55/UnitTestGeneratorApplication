# 🚀 Deployment Guide

## Step 1: GitHub Repository

1. Create a new repository on GitHub
2. Push this project:
```bash
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/unit-test-generator.git
git push -u origin main
```

## Step 2: Deploy Backend (Railway)

1. Go to https://railway.app
2. Click "New Project" → "Deploy from GitHub repo"
3. Select your repository
4. Set root directory: `backend`
5. Add environment variable: `PORT=8080`
6. Deploy!

## Step 3: Deploy Frontend (Vercel)

1. Go to https://vercel.com
2. Click "Add New Project"
3. Import your GitHub repository
4. Set Framework Preset: `Create React App`
5. Set Root Directory: `frontend`
6. Add Environment Variable:
   - Name: `REACT_APP_API_URL`
   - Value: `https://your-railway-backend-url.com`
7. Deploy!

## Step 4: Update CORS

After deployment, update `backend/src/main/java/com/unittestgenerator/config/CorsConfig.java`:
```java
.allowedOrigins("https://your-vercel-frontend-url.vercel.app")
```

## Step 5: College Submission

- [ ] Live URL ready
- [ ] GitHub repo public
- [ ] Demo video (2-3 min screen recording)
- [ ] Project report (PDF)
- [ ] Screenshots in docs/ folder
