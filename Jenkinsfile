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
                    sh "systemctl stop java-notepad.service"
                    sh "NEW_VERSION=$(grep "version =" /var/lib/jenkins/workspace/notepad test/build.gradle | awk -F"'" '{print $2}')"
                    sh "sudo cp /var/lib/jenkins/workspace/notepad test/build/libs/java_notepad-${NEW_VERSION}-SNAPSHOT.jar /var/www/test_ldv_com_usr/data/www/notepad.ldv.com.ua/java_notepad-${NEW_VERSION}-SNAPSHOT.jar"
                    sh "ln -sf /var/www/test_ldv_com_usr/data/www/notepad.ldv.com.ua/java_notepad--${NEW_VERSION}-SNAPSHOT.war /var/www/test_ldv_com_usr/data/www/notepad.ldv.com.ua/java_notepad-latest.war"
                    sh "systemctl start java-notepad.service"
                }
            }
        }
    }
}
