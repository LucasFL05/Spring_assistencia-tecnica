# Usar a imagem do OpenJDK como base
FROM openjdk:21-jdk-slim

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o arquivo JAR gerado para o container
COPY target/assistencia_tecnica-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta 8080
EXPOSE 8080

# Rodar o aplicativo
ENTRYPOINT ["java", "-jar", "app.jar"]
