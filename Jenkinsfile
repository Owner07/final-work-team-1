pipeline {
    agent any

    tools {
        maven "maven 3.9.6"
    }

    parameters {
        choice(choices: ['chrome', 'edge'], description: 'Выберите браузер', name: 'BROWSER')
        choice(choices: ['testng.xml', 'crossBrowser.xml', 'testng-full-workflow.xml'],
               description: 'Выберите XML файл для запуска', name: 'TESTNG_XML')
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Owner07/final-work-team-1.git'
            }
        }

        stage('Run tests Team one') {
            steps {
                script {
                    withCredentials([
                        usernamePassword(
                            credentialsId: 'ui-tests-credentials',
                            usernameVariable: 'UI_USER',
                            passwordVariable: 'UI_PASS'
                        )
                    ]) {
                        // Создаём файл с credentials
                        sh """
                            cat > test.properties << EOF
                            user=\${UI_USER}
                            password=\${UI_PASS}
                            EOF
                        """

                        // Запускаем тесты
                        sh """
                            mvn clean test \\
                                -Dbrowser=${params.BROWSER} \\
                                -DsuiteXmlFile=src/test/resources/${params.TESTNG_XML} \\
                                -DpropertyFile=test.properties
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            // Удаляем временный файл с паролями (даже если тесты упали)
            script {
                sh 'rm -f test.properties || true'
            }
            junit '**/target/surefire-reports/TEST-*.xml'
            allure includeProperties: false, jdk: '', resultPolicy: 'LEAVE_AS_IS', results: [[path: 'target/allure-results']]
        }
    }
}