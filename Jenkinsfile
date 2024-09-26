pipeline {
    agent any

    stages {
        // GitLab에서 코드 체크아웃
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        // 백엔드 빌드 및 Docker 이미지 생성
        stage('Build Backend') {
            steps {
                script {
                    sh 'docker build -t my-backend-app ./backend'
                }
            }
        }

        // 도커 컨테이너 실행
        stage('Run Backend') {
            steps {
                script {
                    // 기존 컨테이너 중지 및 삭제 (이미 실행 중인 경우)
                    sh '''
                    if [ "$(docker ps -q -f name=my-backend-app)" ]; then
                        docker stop my-backend-app
                        docker rm my-backend-app
                    fi
                    '''

                    // 새로운 컨테이너 실행
                    sh '''
                    docker run -d --name my-backend-app -p 8080:8080 my-backend-app
                    '''
                }
            }
        }
    }
}
