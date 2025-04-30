FROM eclipse-temurin:17-jdk-alpine

# Define o diretório de trabalho
WORKDIR /app

# Define o JAVA_HOME explicitamente
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="$JAVA_HOME/bin:$PATH"

# Copia o JAR gerado pelo build para o contêiner
COPY target/*.jar app.jar

# Verifica a versão do Java (opcional, para debug)
RUN java -version

# Define o comando de entrada para executar o JAR
ENTRYPOINT ["java", "-jar", "/app/app.jar"]