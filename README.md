## ShortURL

## Motivação

Projeto criado para prover o serviço (REST) de encurtar uma URL, onde três serviços são oferecidos:
	Encurtar uma URL;
	Recuperar a URL encurtada e
	Um relatório com as 10 URLs mais acessadas.
	
Dessa forma, dada uma palavra URL para o encurtador, será retornada uma nova URL do tipo `http://shortener.be/[Código]`, onde código é a identificação da URL original.
Caso a URL `http://shortener.be/[Código]` seja passada para o serviço de recuperação, será retornada a URL original.
 
O algoritmo utilizado é considerado ingênuo (naive), pois ele atribui um código único alfanumérico à URL original, baseado no ID do banco de dados.

## Linguagem

A linguagem utilizada é Java, com auxílio do framework [Spring boot](https://projects.spring.io/spring-boot/) para geração do container.
Também foi criado um cliente em [AngularJs](https://angularjs.org/) para mostrar o funcionamento do serviço.

## Serviço

Foram implementados três serviços REST que podem ser chamados via navegador da seguinte maneira:

* PUT `http://endereco/encode?originalurl=[URL a ser encurtada]&alias=[Alias opcional para a URL]`
Gera uma URL encurtada no formato `http://shortener.be/[Código]`, caso alias seja passado, o formato ficará `http://shortener.be/[alias]`.

* PUT `http://endereco/decode?shorturl=[http://shortener.be/[Código]]`
Retorna a URL original.

* GET `http://endereco/top10`
Retorna as 10 URLs mais acessadas.

## Compilação

Para facilitar a importação de bibliotecas e a compilação dos arquivos em um único pacote, foi utilizado [Maven](https://maven.apache.org/).
Para compilar gerando o pacote basta executar o comando abaixo na linha de comando.

```mvn -DskipTests compile package```

Na pasta target serão gerados vários arquivos, mas o pacote principal é gerado com o nome `ShortURL-1.0.0-SNAPSHOT.jar`

## Banco de dados

Para facilitar a demostração do funcionamento do registro, o banco de dados usado foi o [H2](http://www.h2database.com/html/main.html).


##Testes

Para os testes foram utilizadas as bibliotecas [JUnit](http://junit.org/junit4/).
Para executar os testes basta escrever na linha de comando abaixo.

 ```mvn test```


## Execução

Para executar o serviço, estando na pasta onde se encontra o arquivo ShortURL-1.0.0-SNAPSHOT.jar, basta rodar na linha de comando o seguinte:

```java -jar ShortURL-1.0.0-SNAPSHOT.jar```

## Cliente

Foi criado um cliente em [AngularJs](https://angularjs.org/) para a total demonstração do funcionamento do serviço.
Para acessá-lo basta colocar no navegador o endereço inicial do serviço, como por exemplo:

`http://localhost:8080`

Aparecerá a página inicial conforme ilustra a imagem abaixo.

![Alt text](https://github.com/RobsonRocha/ShortUrl/blob/master/cliente.jpg "Encurtador de URL")


