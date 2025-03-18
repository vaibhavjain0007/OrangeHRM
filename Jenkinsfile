pipeline {
  agent any

  parameters {
    string description: 'Enter email address', name: 'EMAIL_ADDRESS'
  }

  stages {
    stage('Cleanup') {
      steps {
        script {
          echo "Cleaning up the workspace"
          deleteDir() // cleanup the workspace
        }
      }
    }
    stage('Git Checkout') {
      steps {
        script {
          echo "Sparse checkout to git branch"
          checkout scmGit(
            branches: [[name: '*/test_branch2']],
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

    stage('Build') {
      steps {
        echo "Building the project"
        bat 'mvn clean install'
      }
    }

    stage('Send Email') {
      steps {
        echo "sending email to ${params.EMAIL_ADDRESS}"
        mail body: 'testing', subject: 'testing OrangeHRM', to: "${params.EMAIL_ADDRESS}"
        emailext body: 'testing', subject: 'testing OrangeHRM', to: "${params.EMAIL_ADDRESS}"
      }
    }

    stage ('Test') {
      steps {
        echo "Testing the project"
        bat 'mvn clean test'
      }
    }

    stage('Trigger Downstream Job') {
            steps {
                script {
                  echo "Triggering the downstream job"
                  // Trigger the downstream job
                  build job: 'sample_release_job', wait: false
                  // The 'wait: false' ensures that the downstream job is triggered asynchronously, and the pipeline continues
                  // Use 'wait: true' if you want the pipeline to wait for the downstream job to finish before proceeding
                }
            }
        }
  }

  post {
    always {
      // Archive HTML report (adjust the path to where your report is generated)
      echo "Publishing the report always"
      publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, icon: '', keepAll: false, reportDir: 'target', reportFiles: 'test-output/index.html', reportName: 'HTML Report', reportTitles: '', useWrapperFileDirectly: true])
      junit '**/target/test-output/junitreports/TEST-*.xml'

      // Send an email with the HTML report attached
      //mail subject: 'Build and Test Report for OrangeHRM', body: 'Please find the build and test report attached.', to: "${params.EMAIL_ADDRESS}", attachFiles: 'target/test-output/index.html'
      //emailext subject: 'Build and Test Report for OrangeHRM', body: 'Please find the build and test report attached.', to: "${params.EMAIL_ADDRESS}", attachmentsPattern: 'target/test-output/index.html'
    }

    success {
      echo "The pipeline completed successfully!"  // Message when the build succeeds
      //emailext subject: 'Build and Test Report for OrangeHRM', body: 'Please find the build and test report attached.', to: "${params.EMAIL_ADDRESS}", attachmentsPattern: 'target/test-output/index.html'
    }

    failure {
      echo "The pipeline failed!"  // Message when the build fails
      junit testDataPublishers: [attachments()], testResults: '**/target/test-output/*.xml'
    }
  }
}
