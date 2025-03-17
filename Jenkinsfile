pipeline {
  agent any

  stages {
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
      sh 'mvn clean test'
    }
  }
}
