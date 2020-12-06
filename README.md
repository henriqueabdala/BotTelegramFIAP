# FeiraBot
Uma feira online via Bot do Telegram, onde é possível **consultar** saldo, **inserir** valores, **comprar** frutas e também **consultar a temperatura em graus celsius da cidade de São Paulo**.
## Como usar
São necessários o **token do Telegram** e o **token do WeatherAPI**

##### Token Telegram
Gerar token do Telegram com **BotFather** através da URL
<https://web.telegram.org/#/im?p=@BotFather>

##### Token WeatherAPI
Necessário criação de uma conta no **WeatherAPI** para geração do token requisitado pela API que retorna o clima de São Paulo
<https://www.weatherapi.com>

Após criação do tokens, inserir nos seus respectivos campos dentro do Main.java
```java
String token = "INSERIR_TOKEN_WEATHERAPI";
TelegramBot bot = TelegramBotAdapter.build("INSERIR_TOKEN_TELEGRAM");
```
### Dependências
Dependências do projeto:
| Lib | README |
| ------ | ------ |
| GSON 2.7 | <https://github.com/google/gson> |
| Java Telegram BOT API 2.1.2 | <https://github.com/pengrad/java-telegram-bot-api> |
| okHttp | <https://github.com/square/okhttp> |
| okIO | <https://github.com/square/okio> |
| JSON Simple 1.1 | <https://github.com/fangyidong/json-simple> |

### API
Necessário criação de uma conta para geração do token requisitado pela API que retorna o clima de São Paulo
<https://www.weatherapi.com>

### Colaboradores

| Colaborador | Github |
| ------ | ------ |
| Fernando Gatti | [fernandodgatti][Gatti] |
| Henrique Abdala | [henriqueabdala][Abdala] |
| Iller Boromello | [boromello][Boromello] |


Licença
----
FIAP



[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)


   [dill]: <https://github.com/joemccann/dillinger>
   [git-repo-url]: <https://github.com/joemccann/dillinger.git>
   [john gruber]: <http://daringfireball.net>
   [df1]: <http://daringfireball.net/projects/markdown/>
   [markdown-it]: <https://github.com/markdown-it/markdown-it>
   [Ace Editor]: <http://ace.ajax.org>
   [node.js]: <http://nodejs.org>
   [Twitter Bootstrap]: <http://twitter.github.com/bootstrap/>
   [jQuery]: <http://jquery.com>
   [@tjholowaychuk]: <http://twitter.com/tjholowaychuk>
   [express]: <http://expressjs.com>
   [AngularJS]: <http://angularjs.org>
   [Gulp]: <http://gulpjs.com>
   [Gatti]: <https://github.com/fernandodgatti>
   [Abdala]: <https://github.com/henriqueabdala>
   [Boromello]: <https://github.com/Boromello>
   [PlDb]: <https://github.com/joemccann/dillinger/tree/master/plugins/dropbox/README.md>
   [PlGh]: <https://github.com/joemccann/dillinger/tree/master/plugins/github/README.md>
   [PlGd]: <https://github.com/joemccann/dillinger/tree/master/plugins/googledrive/README.md>
   [PlOd]: <https://github.com/joemccann/dillinger/tree/master/plugins/onedrive/README.md>
   [PlMe]: <https://github.com/joemccann/dillinger/tree/master/plugins/medium/README.md>
   [PlGa]: <https://github.com/RahulHP/dillinger/blob/master/plugins/googleanalytics/README.md>
