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
                script {
                    // ✅ ПРАВИЛЬНО: используем usernamePassword для ваших credentials
                    withCredentials([
                        // Ваши новые credentials (username + password)
                        usernamePassword(
                            credentialsId: 'ui-tests-credentials', // ← Созданные вами
                            usernameVariable: 'UI_USER',
                            passwordVariable: 'UI_PASS'
                        ),
                        // Остальные credentials (если есть)
                        string(credentialsId: 'db-password', variable: 'DB_PASSWORD'),
                        string(credentialsId: 'api-token', variable: 'API_TOKEN'),
                        string(credentialsId: 'admin-password', variable: 'ADMIN_PASSWORD')
                    ]) {
                        git 'https://github.com/Owner07/final-work-team-1.git'

                        sh """
                            mvn clean test \
                                -Dbrowser=${params.BROWSER} \
                                -DsuiteXmlFile=src/test/resources/${params.TESTNG_XML} \
                                -Duser=\${UI_USER} \
                                -Dpassword=\${UI_PASS} \
                                -Ddb_user=pflb-at-read \
                                -Ddb_password=\${DB_PASSWORD} \
                                -Dapi_token=\${API_TOKEN} \
                                -Dusername=admin@pflb.ru \
                                -Dpassword=\${ADMIN_PASSWORD}
                        """
                    }
                }
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