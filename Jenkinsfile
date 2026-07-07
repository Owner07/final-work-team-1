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
                        ),
                        usernamePassword(
                            credentialsId: 'db-credentials',
                            usernameVariable: 'DB_USER',
                            passwordVariable: 'DB_PASS'
                        )
                    ]) {
                        sh """
                            # Создаём файл с credentials
                            cat > test.properties << EOF
                            username=${UI_USER}
                            password=${UI_PASS}
                            db.user=${DB_USER}
                            db.password=${DB_PASS}
                            db.url=jdbc:postgresql://your-db-host:5432/your-db
                            EOF

                            # Запускаем тесты
                            mvn clean test \\
                                -Dbrowser=${params.BROWSER} \\
                                -DsuiteXmlFile=src/test/resources/${params.TESTNG_XML} \\
                                -DpropertyFile=test.properties \\
                                -Dlogback.configurationFile=src/test/resources/logback-test.xml
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                sh '''
                    rm -f test.properties || true
                '''
            }
            junit '**/target/surefire-reports/TEST-*.xml'
            allure includeProperties: false, jdk: '', resultPolicy: 'LEAVE_AS_IS', results: [[path: 'target/allure-results']]
        }
    }
}