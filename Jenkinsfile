pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        timeout(time: 90, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '20', artifactNumToKeepStr: '20'))
    }

    parameters {
        choice(name: 'MAVEN_PROFILE', choices: ['', 'Regression', 'SanityTest', 'Smoke', 'ErrorValidationTests', 'PerformanceTests', 'checkoutProcessTests', 'LoginTests', 'CucumberTests'], description: 'Optional Maven profile / suite selection')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Browser passed to the framework via -DbrowserType')
        string(name: 'CUCUMBER_TAGS', defaultValue: '', description: 'Optional Cucumber tag filter, e.g. @security')
        string(name: 'EXTRA_MAVEN_ARGS', defaultValue: '', description: 'Optional extra Maven args, e.g. -Dtest=MainRunner')
        booleanParam(name: 'SKIP_TESTS', defaultValue: false, description: 'Compile only without executing tests')
    }

    environment {
        MAVEN_COMMON_ARGS = '-B -ntp'
        MAVEN_OPTS = '-Xmx1024m'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    currentBuild.displayName = "#${env.BUILD_NUMBER} ${params.MAVEN_PROFILE ?: 'Default'} ${params.BROWSER}"
                    currentBuild.description = buildSummary(params.MAVEN_PROFILE, params.BROWSER, params.SKIP_TESTS, params.CUCUMBER_TAGS, params.EXTRA_MAVEN_ARGS)
                }
            }
        }

        stage('Validate') {
            steps {
                script {
                    runMaven("${env.MAVEN_COMMON_ARGS} -DskipTests validate")
                }
            }
        }

        stage('Compile Test Sources') {
            steps {
                script {
                    runMaven("${env.MAVEN_COMMON_ARGS} -DskipTests test-compile")
                }
            }
        }

        stage('Run Acceptance Tests') {
            when {
                expression { !params.SKIP_TESTS }
            }
            steps {
                script {
                    String args = "${env.MAVEN_COMMON_ARGS} clean test -DbrowserType=${params.BROWSER}"

                    if (params.MAVEN_PROFILE?.trim()) {
                        args += " -P${params.MAVEN_PROFILE.trim()}"
                    }
                    if (params.CUCUMBER_TAGS?.trim()) {
                        args += " \"-Dcucumber.filter.tags=${params.CUCUMBER_TAGS.trim()}\""
                    }
                    if (params.EXTRA_MAVEN_ARGS?.trim()) {
                        args += " ${params.EXTRA_MAVEN_ARGS.trim()}"
                    }

                    runMaven(args)
                }
            }
        }

        stage('Report Summary') {
            steps {
                script {
                    echo "Pipeline summary: ${buildSummary(params.MAVEN_PROFILE, params.BROWSER, params.SKIP_TESTS, params.CUCUMBER_TAGS, params.EXTRA_MAVEN_ARGS)}"
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true,
                  testResults: 'target/surefire-reports/*.xml, target/failsafe-reports/*.xml',
                  skipMarkingBuildUnstable: false

            archiveArtifacts allowEmptyArchive: true,
                              artifacts: 'target/**, logs/**, report/**, cucumber_report/**, extent-report/**'
        }

        success {
            echo 'Pipeline completed successfully.'
        }

        failure {
            echo 'Pipeline failed — running cleanup.'
            script {
                if (isUnix()) {
                    sh 'docker-compose down --remove-orphans --volumes || true'
                    sh 'pkill -f "java.*surefire" || true'
                } else {
                    bat 'docker-compose down --remove-orphans --volumes || exit 0'
                }
            }
        }

        unstable {
            echo 'Pipeline is unstable. Review archived surefire and Cucumber reports for failed tests.'
        }
    }
}

void runMaven(String args) {
    if (isUnix()) {
        sh "mvn ${args}"
    } else {
        bat "mvn ${args}"
    }
}

String buildSummary(String profile, String browser, boolean skipTests, String tags, String extraArgs) {
    List<String> parts = []
    parts << "profile=${profile?.trim() ? profile.trim() : 'default'}"
    parts << "browser=${browser}"
    parts << "tags=${tags?.trim() ? tags.trim() : 'default'}"
    parts << "skipTests=${skipTests}"
    if (extraArgs?.trim()) {
        parts << "extra=${extraArgs.trim()}"
    }
    return parts.join(', ')
}

