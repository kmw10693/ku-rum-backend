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
                sh 'gradle clean'
            }
        }
        stage('Replace Prod Properties') {
            steps {
                withCredentials([file(credentialsId: 'kurumprod', variable: 'kurumprod')]) {
                    script {
                        sh 'cp $kurumprod ./src/main/resources/application-prod.yml'
                    }
                }
            }
        }
        stage('Build') {
            steps {
                sh 'gradle build -x test'
            }
        }
        stage('Test') {
            steps {
                sh 'gradle test'
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
}