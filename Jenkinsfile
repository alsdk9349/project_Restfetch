pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                script {
                    // 백엔드 빌드
                    dir('backend') {
                        sh 'chmod +x gradlew' // gradlew에 실행 권한 추가
                        sh './gradlew build'   // Gradle 빌드 실행
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    // 도커 이미지 빌드 및 컨테이너 재시작
                    sh 'docker-compose build'
                    sh 'docker-compose down'
                    sh 'docker-compose up -d'
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
