pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Increment Snapshot Version and Tag') {
            steps {
                script {
                    def versionFile = readFile 'version.txt'
                    def versionParts = versionFile.split('\\.')
                    def majorVersion = versionParts[0].toInteger()
                    def minorVersion = versionParts[1].toInteger()
                    def patchVersion = versionParts[2].toInteger()

                    patchVersion++

                    def newVersion = "${majorVersion}.${minorVersion}.${patchVersion}"
                    writeFile file: 'version.txt', text: newVersion

                    sh "git add version.txt"
                    sh "git commit -m 'Increment version to ${newVersion}'"
                    sh "git tag -d v${newVersion} || true"
                    sh "git tag v${newVersion}"
                    sh "git checkout master"
                    sh "git push origin HEAD:master --tags"
                }
            }
        }
        stage('Build and Deploy') {
            steps {
                sh './gradlew build'
                sh '''
                    echo "---------Build start-------"
                    sudo systemctl stop java-notepad.service
                    sudo cp /var/lib/jenkins/workspace/notepadtest/build/libs/java_notepad-0.0.1-SNAPSHOT.war /var/www/test_ldv_com_usr/data/www/notepad.ldv.com.ua/java_notepad-0.0.1-SNAPSHOT.war
                    sudo systemctl start java-notepad.service
                    echo "---------Build finish-------"
                '''
            }
        }
    }
}
