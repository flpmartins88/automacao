# Automação

Uma versão de um sistema que uma vez eu desenvolvi.

Uma tag é um identificador para alguma coisa em um sistema. 

A tag é gerada, produzida e despachada para um cliente.

Uma tag identifica um item e seu serviço mantém todo o histórico eventos.

Atualmente, todas as chamadas que alteram uma tag são sincronas e esse serviço vai disparar eventos informando as alterações para todos os interessados através de eventos 

Lista de projetos que compõe esse sistema:
* **commons:** Utilitários que serão usados por mais de um projeto
* **item:** serviço para o dominio de itens
* **tag:** serviço para gerenciamento de tags
* **stock:** mantém o estoque de todo o sistema
* **production:** responsável por receber um tag e marcar como produzido
* **auth:** servidor de autenticação, usa oauth2 (precisa melhorar, retornar o refresh token) 

### Tópicos:
* **tag_events:** Eventos relacionados a alteração nas tags
* **item_created:** Evento de criação de uma tag. Enviado do dominio de item, serve para disparar algumas coisas em relação ao estoque 
* **item_produced:** Evento para atualização de estoque.

### Futuramente:
* **client:** Carteira de clientes
* **order:** Dominio de pedidos (junta cliente com item e outras coisas). 
* **shipping:** envio dos pedidos para o cliente. As tags devem ser associadas a esse embarque para amarração
* **Eventos:** talvez seja necessário rever eventos de atualização de estoque 
* **Infra:** Rever uso de memória e verificar se é possível subir uma fila para trocar alguns tópicos para fila. (Gostaria de ter usado algum sistema compativel com JMS que tivesse tópico e fila, mas o que tem é muito desatualizado)
                   

## Execução
Para gerar jar

    ./gradlew clean build

Para gerar imagens do docker

    docker-compose build

Para executar

    docker-compose up

Use -d caso você queria que o console fique livre

Agora é possível gerar as imagens docker com o comando
    
    ./gradlew bootBuildImage