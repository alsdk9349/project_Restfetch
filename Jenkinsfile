pipeline {
    agent any  // 어떤 에이전트에서든 실행 가능

    stages {
        stage('Build Backend') {
            steps {
                dir('backend') {  // 백엔드 디렉토리로 이동
                    sh './gradlew clean build'  // Gradle을 사용하여 빌드
                }
            }
        }
        stage('Build Frontend') {
            steps {
                dir('frontend') {  // 프론트엔드 디렉토리로 이동
                    sh './gradlew build'  // 프론트엔드 빌드 명령어
                }
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build -t my-backend-app ./backend'  // 백엔드 Docker 이미지 빌드
                sh 'docker build -t my-frontend-app ./frontend'  // 프론트엔드 Docker 이미지 빌드
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                sh 'kubectl apply -f k8s/deployment.yaml'  // Kubernetes에 배포
            }
        }
    }
    post {
        always {
            // 항상 실행되는 후처리 단계 (예: 빌드 결과 알림 등)
        }
    }
}
