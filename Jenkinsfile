pipeline {
    agent any

    tools {
        maven "maven 3.9.6"
    }

    parameters {
        choice(choices: ['chrome', 'edge'], description: 'Выберите браузер', name: 'BROWSER')
        choice(choices: ['Cross Browser Suite', 'Another Suite'], description: 'Выберите suite для запуска', name: 'SUITE')
    }

    stages {
        stage('Run test Team one') {
            steps {
                git 'https://github.com/Owner07/final-work-team-1.git'
                sh "mvn clean test -Dbrowser=${params.BROWSER} -Dsuite=${params.SUITE}"
            }

            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    allure includeProperties: false, jdk: '', resultPolicy: 'LEAVE_AS_IS', results: [[path: 'target/allure-results']]
                }
            }
        }
    }
}