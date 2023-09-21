pipeline {
    agent any
    stages {
        // ... (інші етапи)
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

                    // Оновлення версії в build.gradle
                    def buildGradleContent = readFile 'build.gradle'
                    def updatedBuildGradleContent = buildGradleContent.replaceAll("version = '.*'", "version = '${newVersion}-SNAPSHOT'")
                    writeFile file: 'build.gradle', text: updatedBuildGradleContent

                    sh "git add version.txt build.gradle"
                    sh "git commit -m 'Increment version to ${newVersion}'"
                    sh "git tag -d v${newVersion} || true"
                    sh "git tag v${newVersion}"
                    sh "git checkout master"
                    sh "git push origin HEAD:master --tags"
                }
            }
        }
    }
}
