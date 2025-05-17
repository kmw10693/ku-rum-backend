pipeline{
    agent any
    environment {
        SCRIPT_PATH = '/var/jenkins_home/custom/kurum'
    }
    tools {
        gradle 'kurum'
    }
    stages{
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Prepare'){
            steps {
                sh './gradlew clean'
                sh './gradlew build'
            }
        }
        stage('Replace Prod Properties') {
            steps {
                withCredentials([
                    file(credentialsId: 'kurumprod', variable: 'kurumprod'),
                    file(credentialsId: 'firebase', variable: 'firebase')
                ]) {
                    script {
                        sh 'cp $kurumprod ./src/main/resources/application-prod.yml'
                        sh 'cp $firebase ./src/main/resources/kuroom-90fb5-firebase-adminsdk-fbsvc-f264f66c64.json'
                    }
                }
            }
        }
        stage('Build') {
            steps {
                sh './gradlew clean'
                sh './gradlew build --debug'
            }
        }
        stage('Deploy') {
            steps {
                sh '''
                    cp ./docker/docker-compose.blue.yml ${SCRIPT_PATH}
                    cp ./docker/docker-compose.green.yml ${SCRIPT_PATH}
                    cp ./docker/Dockerfile ${SCRIPT_PATH}
                    cp ./scripts/deploy.sh ${SCRIPT_PATH}
                    cp ./build/libs/*.jar ${SCRIPT_PATH}
                    chmod +x ${SCRIPT_PATH}/deploy.sh
                    ${SCRIPT_PATH}/deploy.sh
                '''
            }
        }
    }
    post {
        success {
            withCredentials([string(credentialsId: 'Discord-Webhook', variable: 'DISCORD')]) {
                discordSend description: "✅ 성공: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})\n최근 커밋: '${env.GIT_COMMIT_MESSAGE}'",
                    footer: "footer 표시",
                    link: env.BUILD_URL,
                    result: currentBuild.currentResult,
                    title: "젠킨스 JOB",
                    webhookURL: "$DISCORD"
            }
        }
        failure {
            withCredentials([string(credentialsId: 'Discord-Webhook', variable: 'DISCORD')]) {
                discordSend description: "❌ 실패: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})\n최근 커밋: '${env.GIT_COMMIT_MESSAGE}'",
                    footer: "footer 표시",
                    link: env.BUILD_URL,
                    result: currentBuild.currentResult,
                    title: "젠킨스 JOB",
                    webhookURL: "$DISCORD"
            }
        }
    }
}
