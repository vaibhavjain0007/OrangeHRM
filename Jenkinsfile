pipeline {
  agent any

  stages {
    stage('Send Email') {
      steps {
        mail body: 'testing', subject: 'testing OrangeHRM', to: 'mfsi.vaibhav@gmail.com'
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
