# Projeto API Labs

Api que contém o cadastro, listagem e deleção de pessoas.

##Dependências externas

Como a Api faz integração com o Facebook por isso é necessário ter um access_token válido para realizar as consultas.

## Tecnologias utilizadas

* Linguagem Java - Versão 1.8 (Oracle 1.8.0_121)

* Gradle 5 - Ferramenta de Build

* Versionador - Git

* Banco de Dados - Mongo (emmbed tanto para local quanto para testes)

* Spring Boot (2.1.5.RELEASE) - Setup de projeto

* Open Feign - Para integração com Apis externas

* Spring Boot Test - Para os testes de integração

# Documentação através do swagger (versão 2.9.2)

O projeto possui um documentação de API através do swagger.

Para acessar:

### Api: beer-style-resource : Beer Style Resource

http://localhost:8080/api/swagger-ui.html


## Para exeuctar o build, testes e realizar o start do programa

Acesse o arquivo:
/labs/src/main/resources/application.properties e troque: `YOUR_FACEBOOK_KEY_HERE` pelo seu access_token do Facebook.

Para executar o ambiente local é necessario ter instalado alguma versão do java 8.

Execute o comando abaixo para rodar os testes:

```
./gradlew test
```

Para executar o build:

```
./gradlew build
```

Acesse a pasta build/libs e execute o comando para rodar o programa:

```
java -jar labs-0.0.1-SNAPSHOT.jar
```

Também é possível rodar a Api via docker(Necessita do docker instalado na máquina).

Para executar no Linux e Mac execute os comando abaixo:

```
 chmod +x run.sh  
 run.sh
```
Para ambiente Windows. Execute os comando abaixo:
```
./gradlew build
docker rmi -f labs
docker build -t labs .
docker run --name labs -p 8080:8080 -t labs
```
