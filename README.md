# Automação

Uma versão de um sistema que uma vez eu desenvolvi.

Uma tag é um identificador para alguma coisa em um sistema. 

A tag é gerada, produzida e despachada para um cliente.

Uma tag identifica um item e seu serviço mantém todo o histórico eventos.

Atualmente, todas as chamadas que alteram uma tag são sincronas e esse serviço vai disparar eventos informando as alterações para todos os interessados através de eventos 

Lista de projetos que compõe esse sistema:
* commons: Utilitários que serão usados por mais de um projeto
* item: serviço para o dominio de itens
* production: responsável por receber um tag e marcar como produzido 

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