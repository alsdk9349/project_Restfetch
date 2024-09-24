pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                script {
                    sh 'docker build -t my-backend-app ./backend'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                script {
                    sh 'docker build -t my-frontend-app ./frontend'
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Backend 배포
                    sh 'kubectl apply -f backend/k8s-backend.yaml'
                    // Frontend 배포
                    sh 'kubectl apply -f frontend/k8s-frontend.yaml'
                }
            }
        }
    }
}
