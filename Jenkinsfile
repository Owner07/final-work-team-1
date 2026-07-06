pipeline {
    agent any

    tools {
        maven "maven 3.9.6"
    }

    parameters {
        choice(choices: ['chrome', 'edge'], description: 'Выберите браузер', name: 'BROWSER')
        choice(choices: ['testng.xml', 'testng-chrome.xml', 'testng-edge.xml', 'testng-smoke.xml', 'testng-regression.xml'],
               description: 'Выберите XML файл для запуска', name: 'TESTNG_XML')
    }

    stages {
        stage('Run test Team one') {
            steps {
                git 'https://github.com/Owner07/final-work-team-1.git'
                sh "mvn clean test -Dbrowser=${params.BROWSER} -DsuiteXmlFile=src/test/resources/${params.TESTNG_XML}"
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