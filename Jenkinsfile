pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                timeout(time: 50, unit: 'SECONDS') {
                    sh "chmod +x -R ${env.WORKSPACE}"
                    sh './completeBuild.sh'
                }
            }
        }
    }
    post {
        always {
            sh 'echo "pipeline complete"'
        }
    }
}
