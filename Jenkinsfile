pipeline {
    agent any
    stages {

        stage('Increment Snapshot Version and Tag') {
            when {
                branch 'master'
            }
            steps {
                script {
                    def oldVersion = readFile('build.gradle').find(/version = '(.*)'/)?.replaceAll(/version = '(.*)'/, '$1').trim()
                    def newVersion = incrementVersion(oldVersion)
                    sh "./gradlew build -PnewVersion=${newVersion}"
                    sh "git add build.gradle"
                    sh "git commit -m 'Increment snapshot version'"
                    sh "git tag ${newVersion}"
                    sh "git push origin master"
                    sh "git push origin ${newVersion}"
                }
            }
        }
    }
}
