# Automação

## Execução
Para gerar jar

    ./gradlew clean build

Para gerar imagens do docker

    docker-compose build

Para executar

    docker-compose up

Use -d caso você queria que o console fique livre

-- -
Obs: Não use o Java 14, dá erro na execução dos testes unitários. ClassNotFound em uma DefaultClassLoaderQualquerCoisa.