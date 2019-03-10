@Library('pipeline-library-demo')_

node ('master') {
    stage('Demo') {
      echo 'Hello World'
      sayHello 'Dave'
    }
    
    stage('Build') {
      echo 'Build my cool project'
    }
}
