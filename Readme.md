# springJavaFX

Projeto base para aplicações desktop combinando **Spring Boot** + **JavaFX**, usando
[FxWeaver](https://github.com/rgielen/javafx-weaver) para integrar o ciclo de vida do JavaFX
com o container de injeção de dependências do Spring.

O objetivo é servir como ponto de partida para novas aplicações desktop: já vem com
persistência configurada (SQLite), navegação entre telas centralizada, e um fluxo de
exemplo  demonstrando o padrão de
Model / Repository / Service / Controller a ser seguido no restante do projeto.

## Stack e versões

| Tecnologia | Versão |
|---|---|
| Java | 25 |
| Spring Boot | 4.1.0 |
| JavaFX (graphics + fxml) | 26 |
| FxWeaver (javafx-weaver-spring-boot-starter) | 2.0.1 |
| CSSFX (hot-reload de CSS em dev) | 11.5.1 |
| SQLite JDBC (xerial) | 3.53.2.1 |
| Hibernate Community Dialects (SQLiteDialect) | gerenciado pelo Spring Boot BOM |
| Spring Data JPA | gerenciado pelo Spring Boot BOM |



## Arquitetura

- **`JavaFXApplication`** — ponto de entrada JavaFX (`Application`). No `init()`, resolve o
  diretório de dados da aplicação (`AppDataLocator`) e sobe o contexto Spring. No `start()`,
  publica o evento `StageReadyEvent` assim que o `Stage` principal está pronto.
- **`StageReadyEventListener`** — escuta o `StageReadyEvent`, registra o `Stage` no
  `SceneNavigator` e carrega a primeira tela.
- **`SceneNavigator`** — componente central para trocar de tela. Encapsula
  `FxWeaver.loadView(...)` + `Scene` + CSS, evitando repetir essa lógica em cada controller.
- **`SessionContext`** — bean simples para carregar dados de uma tela para outra (o FxWeaver
  não permite passar parâmetros diretamente ao carregar uma view).
- **`AppDataLocator`** — resolve o diretório correto de dados por sistema operacional
  (`%LOCALAPPDATA%` no Windows, `Application Support` no macOS, `~/.local/share` no Linux),
  usado para o arquivo do banco SQLite ficar fora da pasta de instalação.
- **Camadas convencionais**: `model` (entidades JPA), `repository` (Spring Data JPA),
  `service` (regra de negócio), `view/controllers` (controllers JavaFX/FxWeaver).

## Banco de dados

- SQLite, arquivo único, sem servidor.
- Local do arquivo: resolvido em runtime pelo `AppDataLocator`, fora da pasta de instalação
  (importante para builds empacotados em instalador/MSI, onde `Program Files` costuma exigir
  privilégio de administrador para escrita).
- Ajuste `VENDOR` e `APP_NAME` em `JavaFXApplication` para definir o nome da pasta de dados
  antes de distribuir a aplicação.
- `spring.jpa.hibernate.ddl-auto=update` está ativo — adequado para desenvolvimento; avalie
  migrations (ex: Flyway) antes de ir para produção com dados reais de cliente.

## Como rodar localmente

### Pré-requisitos

- JDK 25 instalado (confira com `java -version`)
- Não é necessário ter Maven instalado — o projeto usa o Maven Wrapper (`mvnw` / `mvnw.cmd`)

### Rodando

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

Para rodar com o profile `dev` (habilita hot-reload de CSS via CSSFX):

```bash
# Linux / macOS
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Windows
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

Alternativamente, na IDE (IntelliJ, Eclipse, etc.), rode a classe `SpringJavaFxApplication`
diretamente, configurando o profile `dev` na *run configuration* se desejar.

### Build

```bash
./mvnw clean package
```

O artefato final é gerado em `target/`.

> **Nota:** após qualquer alteração em arquivos `.fxml`, `.css` ou de configuração, prefira
> `mvnw clean compile` (ou rebuild pela IDE) antes de rodar novamente — o JavaFX carrega os
> recursos a partir de `target/classes`, e um build "sujo" pode reaproveitar versões antigas.

## Como adicionar uma nova tela

1. **Criar o FXML** em `src/main/resources/views/NomeDaTela.fxml`, com `fx:controller`
   apontando o nome **totalmente qualificado** do controller (obrigatório para o FxWeaver
   resolver o bean via Spring):
   ```xml
   fx:controller="com.mrrezende.springJavaFX.view.controllers.NomeDaTelaController"
   ```

2. **Criar o Controller** em `view/controllers/NomeDaTelaController.java`:
    - `@Component` + `@FxmlView("/views/NomeDaTela.fxml")`
    - Campos `@FXML` correspondentes aos `fx:id` do FXML
    - Métodos `@FXML` para os `onAction` dos botões
    - Se precisar navegar para outra tela, injete `SceneNavigator` no construtor
    - Se precisar de dado vindo de outra tela, injete `SessionContext` e leia o valor em um
      método `@FXML private void initialize()` (chamado automaticamente pelo `FXMLLoader`
      após a injeção dos campos)

3. **(Opcional) CSS próprio da tela**: crie `views/css/nome-da-tela.css` se a tela precisar
   de um estilo diferente do restante da aplicação.

4. **Disparar a navegação** de onde fizer sentido (geralmente a partir de outro controller):
   ```java
   sceneNavigator.navigateTo(NomeDaTelaController.class, "/views/css/nome-da-tela.css");
   ```
   Sem CSS próprio: `sceneNavigator.navigateTo(NomeDaTelaController.class)`.

5. **Passar dados entre telas**, se necessário: grave o dado no `SessionContext` antes de
   chamar `navigateTo`, e leia no `initialize()` da tela seguinte. Para fluxos com muitos
   dados diferentes, prefira criar um objeto de estado dedicado em vez de sobrecarregar o
   `SessionContext` genérico.

6. **Regra de negócio e persistência**: Siga o padrão do exemplo
   `User`:
    - `model/` — entidade JPA
    - `repository/` — interface `JpaRepository`
    - `service/` — orquestra o repository e concentra a regra de negócio; é isso que o
      controller injeta e chama

### Exemplo de referência no próprio projeto

O fluxo `Main.fxml` → `MainWindowController` → `Welcome.fxml` → `WelcomeController`
implementa exatamente esses passos: captura um nome, persiste via `UserService`/
`UserRepository`/`User`, guarda o id no `SessionContext`, navega via `SceneNavigator` com
CSS próprio (`welcome.css`), e busca o registro no banco na tela seguinte. Use-o como
modelo para novas telas.

## Estrutura de pastas

```
src/main/java/com/mrrezende/springJavaFX/
├── config/            # Configuração Spring (AppConfig, AppDataLocator)
├── model/              # Entidades JPA
├── repository/         # Interfaces Spring Data JPA
├── service/             # Regras de negócio
├── view/
│   ├── controllers/    # Controllers JavaFX (FxWeaver)
│   ├── event/           # StageReadyEvent / listener de inicialização
│   └── navigation/      # SceneNavigator e SessionContext
├── JavaFXApplication.java       # Bootstrap JavaFX + Spring
└── SpringJavaFxApplication.java # Classe @SpringBootApplication

src/main/resources/
├── views/               # Arquivos .fxml
│   └── css/              # Estilos por tela
└── application.properties
```
