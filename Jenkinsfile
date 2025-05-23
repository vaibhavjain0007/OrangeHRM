pipeline {
    agent any
    stages {
        stage ('Build Jar') {
            steps {
                bat "mvn clean package -DskipTests"
            }
        }
        stage ('Build Image') {
            steps {
                bat "docker build -t vaibhavj007/selenium-docker -f ./Dockerfile.txt ."
            }
        }
        stage ('Push Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-cred', passwordVariable: 'pass', usernameVariable: 'user')]) {
                    //sh
                    bat "docker login --username=${user} --password=${pass}"
                    bat "docker push vaibhavj007/selenium-docker:latest"
                }
            }
        }
    }

    post {
        always {
          // Archive HTML report (adjust the path to where your report is generated)
          echo "Publishing the report always"
          // publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, icon: '', keepAll: false, reportDir: 'target', reportFiles: 'test-output/index.html', reportName: 'HTML Report', reportTitles: '', useWrapperFileDirectly: true])
          // junit '**/target/test-output/junitreports/TEST-*.xml'

          // Send an email with the HTML report attached
          // mail subject: 'Build and Test Report for OrangeHRM', body: 'Please find the build and test report attached.', to: "${params.EMAIL_ADDRESS}", attachFiles: 'target/test-output/index.html'
          // emailext subject: 'Build and Test Report for OrangeHRM', body: 'Please find the build and test report attached.', to: "${params.EMAIL_ADDRESS}", attachmentsPattern: 'target/test-output/index.html'
        }
    }
}