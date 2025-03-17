pipeline {
  agent any

  parameters {
    string description: 'Enter email address', name: 'EMAIL_ADDRESS'
  }

  stages {
    stage('Send Email') {
      steps {
        echo 'sending email to ${params.EMAIL_ADDRESS}'
        mail body: 'testing', subject: 'testing OrangeHRM', to: '${params.EMAIL_ADDRESS}'
      }
    }
    
    stage('Checkout') {
      steps {
                script {
                    checkout scmGit(
                        branches: [[name: '*/test_branch']],
                        extensions: [
                            sparseCheckout([
                                [path: 'src'],
                                [path: 'pom.xml'],
                                [path: 'testng.xml']
                            ])
                        ],
                        userRemoteConfigs: [[url: 'https://github.com/vaibhavjain0007/OrangeHRM']]
                    )
                }
            }
    }

    stage ('Test') {
      steps {
        sh 'mvn clean test'
      }
    }
  }
}
