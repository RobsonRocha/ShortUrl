## ShortURL

## Motiva��o

Projeto criado para prover o servi�o (REST) de encurtar uma URL, onde tr�s servi�os s�o oferecidos:
	Encurtar uma URL;
	Recuperar a URL encurtada e
	Um relat�rio com as 10 URLs mais acessadas.
	
Dessa forma, dada uma palavra URL para o encurtador, ser� retornada uma nova URL do tipo `http://shortener.be/[C�digo]`, onde c�digo � a identifica��o da URL original.
Caso a URL `http://shortener.be/[C�digo]` seja passada para o servi�o de recupera��o, ser� retornada a URL original.
 
O algoritmo utilizado � considerado ing�nuo (naive), pois ele atribui um c�digo �nico alfanum�rico � URL original, baseado no ID do banco de dados.

## Linguagem

A linguagem utilizada � Java, com aux�lio do framework [Spring boot](https://projects.spring.io/spring-boot/) para gera��o do container.
Tamb�m foi criado um cliente em [AngularJs](https://angularjs.org/) para mostrar o funcionamento do servi�o.

## Servi�o

Foram implementados tr�s servi�os REST que podem ser chamados via navegador da seguinte maneira:

* PUT `http://endereco/encode?originalurl=[URL a ser encurtada]&alias=[Alias opcional para a URL]`
Gera uma URL encurtada no formato `http://shortener.be/[C�digo]`, caso alias seja passado, o formato ficar� `http://shortener.be/[alias]`.

* PUT `http://endereco/decode?shorturl=[http://shortener.be/[C�digo]]`
Retorna a URL original.

* GET `http://endereco/top10`
Retorna as 10 URLs mais acessadas.

## Compila��o

Para facilitar a importa��o de bibliotecas e a compila��o dos arquivos em um �nico pacote, foi utilizado [Maven](https://maven.apache.org/).
Para compilar gerando o pacote basta executar o comando abaixo na linha de comando.

```mvn -DskipTests compile package```

Na pasta target ser�o gerados v�rios arquivos, mas o pacote principal � gerado com o nome `ShortURL-1.0.0-SNAPSHOT.jar`

## Banco de dados

Para facilitar a demostra��o do funcionamento do registro, o banco de dados usado foi o [H2](http://www.h2database.com/html/main.html).


##Testes

Para os testes foram utilizadas as bibliotecas [JUnit](http://junit.org/junit4/).
Para executar os testes basta escrever na linha de comando abaixo.

 ```mvn test```


## Execu��o

Para executar o servi�o, estando na pasta onde se encontra o arquivo ShortURL-1.0.0-SNAPSHOT.jar, basta rodar na linha de comando o seguinte:

```java -jar ShortURL-1.0.0-SNAPSHOT.jar```

## Cliente

Foi criado um cliente em [AngularJs](https://angularjs.org/) para a total demonstra��o do funcionamento do servi�o.
Para acess�-lo basta colocar no navegador o endere�o inicial do servi�o, como por exemplo:

`http://localhost:8080`

Aparecer� a p�gina inicial conforme ilustra a imagem abaixo.

![Alt text](https://github.com/RobsonRocha/ShortUrl/blob/master/cliente.jpg "Encurtador de URL")


