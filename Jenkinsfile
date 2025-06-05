pipeline {
  agent { label 'jenkins-agent' }

  environment {
    MAVEN_OPTS = '-Xmx1024m'
    DOCKER_HUB_USER = 'rima603'
    DOCKER_HUB_PASSWORD = credentials('dockerhub')
    SONARQUBE_SERVER = 'sonarqube'
    SONAR_HOST_URL = 'http://192.168.40.111:9000'
    SONAR_AUTH_TOKEN = credentials('jenkins-sonarqube-token')
  }

  stages {

    stage("Cleanup Workspace") {
      steps {
        cleanWs()
      }
    }

    stage("Checkout Application Code") {
      steps {
        git(
          branch: 'main',
          credentialsId: 'github',
          url: 'https://github.com/rima-gif/projetfinal.git'
        )
      }
    }

    stage("Build Application") {
      parallel {
        stage("SpringBoot") {
          steps {
            dir('ebanking-backend') {
              sh 'mvn clean install -DskipTests=true'
            }
          }
        }
        stage("Angular") {
          steps {
            dir('ebanking-frontend') {
              sh 'npm install'
              sh './node_modules/.bin/ng build --configuration production'
            }
          }
        }
      }
    }

    stage('Start MySQL for Tests') {
      steps {
        script {
          sh '''
            docker rm -f mysql-test || true

            docker run -d --name mysql-test -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=E-Bank -p 3306:3306 mysql:8.0

            echo "⏳ Attente de démarrage de MySQL..."
            for i in $(seq 1 20); do
              if docker exec mysql-test mysqladmin ping -h127.0.0.1 -proot > /dev/null 2>&1; then
                echo "✅ MySQL est prêt après $i tentatives."
                break
              else
                echo "🔄 Tentative $i : MySQL pas encore prêt..."
                sleep 3
              fi
            done

            if ! docker exec mysql-test mysqladmin ping -h127.0.0.1 -proot > /dev/null 2>&1; then
              echo "❌ Échec : MySQL n'est pas prêt après 20 tentatives."
              docker logs mysql-test
              exit 1
            fi
          '''
        }
      }
    }

    stage("Run Backend Unit Tests (JUnit)") {
      steps {
        dir('ebanking-backend') {
          // Ici on lance les tests unitaires JUnit avec Maven
          sh 'mvn test'
        }
      }
      post {
        always {
          junit 'ebanking-backend/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Stop MySQL') {
      steps {
        sh '''
          docker stop mysql-test || true
          docker rm mysql-test || true
        '''
      }
    }

stage("SonarQube Analysis") {
      steps {
        dir('ebanking-backend') {
          withSonarQubeEnv('sonarqube') {
            sh "mvn sonar:sonar -Dsonar.host.url=${env.SONAR_HOST_URL} -Dsonar.login=${env.SONAR_AUTH_TOKEN}"
          }
        }
      }
    }

    stage("Build Docker Images") {
      steps {
        script {
          dir('ebanking-backend') {
            sh """
              docker build -t rima603/backprojet1:${BUILD_NUMBER} .
              docker tag rima603/backprojet1:${BUILD_NUMBER} rima603/backprojet1:latest
            """
          }
          dir('ebanking-frontend') {
            sh """
              docker build -t rima603/frontprojet1:${BUILD_NUMBER} .
              docker tag rima603/frontprojet1:${BUILD_NUMBER} rima603/frontprojet1:latest
            """
          }
        }
      }
    }

    stage("Trivy Security Scan") {
      steps {
        script {
          sh '''
            echo "🔍 Trivy scan rapide sur les images Docker..."

            mkdir -p $HOME/.cache/trivy

            docker run --rm \
              -v /var/run/docker.sock:/var/run/docker.sock \
              -v $HOME/.cache/trivy:/root/.cache/ \
              aquasec/trivy image \
              --scanners vuln \
             --skip-db-update \
              --timeout 10m \
              --severity HIGH,CRITICAL \
              rima603/backprojet1:${BUILD_NUMBER} || true

            docker run --rm \
              -v /var/run/docker.sock:/var/run/docker.sock \
              -v $HOME/.cache/trivy:/root/.cache/ \
              aquasec/trivy image \
              --scanners vuln \
              --skip-db-update \
              --timeout 2m \
              --severity HIGH,CRITICAL \
              rima603/frontprojet1:${BUILD_NUMBER} || true
          '''
        }
      }
    }

    stage("Push Docker Images to Docker Hub") {
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh """
            echo "${DOCKER_PASS}" | docker login -u "${DOCKER_USER}" --password-stdin
            docker push rima603/backprojet1:${BUILD_NUMBER}
            docker push rima603/backprojet1:latest
            docker push rima603/frontprojet1:${BUILD_NUMBER}
            docker push rima603/frontprojet1:latest
          """
        }
      }
    }

    stage("Update Kubernetes Manifests") {
      steps {
        dir('k8s-manifests') {
          git branch: 'main', credentialsId: 'github', url: 'https://github.com/rima-gif/k8s-manifests.git'

          sh """
            sed -i 's|image: rima603/backprojet1:.*|image: rima603/backprojet1:${BUILD_NUMBER}|' backend/deployment.yaml
            sed -i 's|image: rima603/frontprojet1:.*|image: rima603/frontprojet1:${BUILD_NUMBER}|' frontend/deployment.yaml
          """

          withCredentials([usernamePassword(credentialsId: 'github', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_TOKEN')]) {
            sh '''
              git config user.email "achourryma971@gmail.com"
              git config user.name "rima-gif"
              git remote set-url origin https://${GIT_USER}:${GIT_TOKEN}@github.com/rima-gif/k8s-manifests.git
              git add backend/deployment.yaml frontend/deployment.yaml
              git commit -m "Update image tags to build ${BUILD_NUMBER}" || echo "No changes to commit"
              git push origin main
            '''
          }
        }
      }
    }
  }

post {
  success {
    echo "✅ Pipeline exécuté avec succès !"
    slackSend (
      channel: '#test-jekins',
      message: "✅ *Pipeline réussi* : `${env.JOB_NAME}` - Build #${env.BUILD_NUMBER}",
      color: 'good'
    )
  }
  failure {
    echo "❌ Pipeline échoué. Vérifie les logs et corrige les erreurs."
    slackSend (
      channel: '#test-jekins',
      message: "❌ *Pipeline échoué* : `${env.JOB_NAME}` - Build #${env.BUILD_NUMBER}.\nVoir les logs: ${env.BUILD_URL}",
      color: 'danger'
    )
  }
}
}
