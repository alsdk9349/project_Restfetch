pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                script {
                    // 백엔드 빌드
                    dir('backend') {
                        sh 'chmod +x gradlew'
                        sh './gradlew build'
                    }
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // Docker 이미지 빌드
                    sh 'docker build -t my-backend-app:latest ./backend'
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    // Docker Compose를 사용하여 서비스 배포
                    sh 'docker-compose up -d --build'
                }
            }
        }
    }
    post {
        success {
            echo '배포가 성공적으로 완료되었습니다.'
        }
        failure {
            echo '배포에 실패했습니다.'
        }
    }
}
