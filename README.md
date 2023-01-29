# cook-plugin-springboot

----
## O que é?
> Mais um plugin do projeto [Cook](https://github.com/itakenami/cook) para geração de código fonte CRUD de forma simples para projetos criados com o framework [Springboot](https://spring.io/projects/spring-boot). A construção é baseada em entidades do banco de dados, então basta apenas que seja realizada a configuração de conexão no arquivo "src/main/resources/application.properties" do seu projeto [Springboot]([https://laravel.com/](https://spring.io/projects/spring-boot)).

----
## Como usar?
>Baixe o [Cook](https://github.com/itakenami/cook) e crie a variável de ambiente de sistema "COOK_HOME" apontando para pasta "bin" do COOK:
Ex: COOK_HOME = C:\dev\cook\bin
>Complemente a variável de sistema Path com a variável criada adicionando %COOK_HOME% no final das variáveis existentes.

>Com o [Cook](https://github.com/itakenami/cook) instalado e configurado, é possível utilizar os comandos:

```
cook install springboot
```
para instalar o plugin,
```
cook springboot model
```
cook springboot repository
```
cook springboot service e
```
cook springboot controller.
