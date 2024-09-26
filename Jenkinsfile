pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://lab.ssafy.com/s11-mobility-smarthome-sub1/S11P21C209.git', credentialsId: 'e6553265-42ed-4359-87e3-3d27fca01aae'
            }
        }
        stage('Build Backend') {
            steps {
                sh 'cd backend && ./gradlew build' // 백엔드 Gradle 빌드
                sh 'docker build -t backend-app -f backend/Dockerfile backend/' // 백엔드 Docker 이미지 빌드
            }
        }
        stage('Deploy Backend') {
            steps {
                sh 'docker ps -q --filter "name=backend-app" | xargs -r docker stop'
                sh 'docker ps -aq --filter "name=backend-app" | xargs -r docker rm'
                sh 'docker run -d -p 8080:8080 --name backend-app backend-app'
            }
        }
//         stage('Build Frontend') {
//             steps {
//                 sh 'docker build -t frontend-app -f frontend/Dockerfile frontend/' // 프론트엔드 Docker 이미지 빌드
//             }
//         }
//         stage('Deploy Frontend') {
//             steps {
//                 sh 'docker ps -q --filter "name=frontend-app" | xargs -r docker stop'
//                 sh 'docker ps -aq --filter "name=frontend-app" | xargs -r docker rm'
//                 sh 'docker run -d -p 80:80 --name frontend-app frontend-app'
//             }
//         }
    }
}
