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
                    // 기존 컨테이너 종료 및 삭제
                    sh 'docker stop backend || true' // 컨테이너가 실행 중이면 정지
                    sh 'docker rm backend || true'   // 컨테이너 삭제

                    // 새 컨테이너 실행
                    sh 'docker run -d --name backend -p 8080:8080 ' +
                       '-e SPRING_DATASOURCE_URL=jdbc:mysql://172.26.4.40:3306/restfetch ' +
                       '-e SPRING_DATASOURCE_USERNAME=root ' +
                       '-e SPRING_DATASOURCE_PASSWORD=Rnwheo1234! ' +
                       '-e SPRING_REDIS_HOST=172.26.4.40 ' +
                       '-e SPRING_REDIS_PORT=6379 ' +
                       'my-backend-app:latest'
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
