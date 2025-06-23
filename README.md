# ![Diagrama dos Módulos](img/kelari_413_163.png)

> Automatize a geração de testes para APIs REST em Spring Boot, com anotações declarativas e sem esforço manual!

---

## ✨ Visão Geral

**Kelari API Test Generator** é uma biblioteca Java que gera testes automatizados para endpoints REST com base em anotações simples e legíveis diretamente nos seus controllers \ resources.

Com ela, você documenta e define os cenários esperados de cada endpoint de forma declarativa, e a biblioteca gera automaticamente os métodos de teste utilizando `WebTestClient`.

> 💡 Com Kelari, você foca no que importa — os testes fluem naturalmente.

---

## 🧠 O que é Design by Contract?

O conceito de **Design by Contract (DbC)** parte da ideia de que um software funciona melhor quando as partes que se comunicam (por exemplo, um método e seu consumidor) **assumem compromissos explícitos entre si** — como um contrato formal.

Cada endpoint da API, nesse contexto, representa um "contrato" que descreve:

- 📥 **O que ele espera receber** (parâmetros, headers, cookies etc.)
- 📤 **O que ele promete retornar** (status, corpo da resposta)
- ⚠️ **O que ele garante em caso de erro** (exceções tratadas, status esperados)

> Com o **Kelari**, você declara esses contratos usando anotações, e os testes são gerados automaticamente para garantir que **cada contrato está sendo cumprido**. Design by Contract é mais do que uma técnica — é uma mentalidade de desenvolvimento responsável, onde o código cumpre o que promete.

### Exemplo de contrato declarado:

```java
@ApiTestCase(
        displayName = "✅ Deve retornar 200 quando o cliente for encontrado",
        expectedStatusCodes = 200,
        requiresAuth = true
  ...
          )
```

---

## 🚀 Principais Recursos

- ✅ Geração automática de testes unitários para endpoints HTTP
- 🧪 Definição de casos de teste via anotação `@ApiTestCase`
- ⚖️ Possibilidade de carregar dados com a interface `DataLoad`
- 🔍 Compatível com Spring WebFlux
- ⚙️ Personalização de status esperados, timeout e ordem de execução

---

## 📦 Instalação

Adicione ao seu `pom.xml` (em breve no Maven Central), por agora no Github Packages:

```xml
<repositories>
    <repository>
        <id>github</id>
        <name>GitHub Kelari Maven</name>
        <url>https://maven.pkg.github.com/agsn10/kelari-spring-api-test-generator</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>io.github.kelari.atg</groupId>
        <artifactId>kelari-spring-api-test-generator</artifactId>
        <version>1.1.6</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
            <configuration>
                <fork>true</fork>
                <compilerArgs>
                    <arg>-Xplugin:KelariApiTestGeneratorPlugin</arg>
                </compilerArgs>
                <annotationProcessorPaths>
                    <path>
                        <groupId>io.github.kelari.atg</groupId>
                        <artifactId>kelari-spring-api-test-generator</artifactId>
                        <version>1.1.6</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```
### 🔐 3. Autenticar no GitHub Packages

O GitHub requer autenticação mesmo para **repositórios públicos**. É necessário configurar o `settings.xml` do Maven com as credenciais corretas.

---

#### a. Criar ou obter um token no GitHub

1. Acesse: [https://github.com/settings/tokens](https://github.com/settings/tokens)
2. Crie um **token clássico (Classic Token)** com os seguintes escopos:

- `read:packages`

---

#### b. Adicionar no arquivo `~/.m2/settings.xml`

```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>SEU_USUARIO_GITHUB</username>
            <password>SEU_TOKEN_GITHUB</password>
        </server>
    </servers>
</settings>
```
> ⚠️ O valor de &lt;id&gt; deve ser exatamente github, pois ele é usado para associar com o <repository> declarado no pom.xml.

---

# ✨ Como a mágica acontece?

> 💡Comando para gerar: mvn clean compile  
> 💡Comando para testar: mvn test

---

## 🧑‍💻 Exemplo de Uso

### 1. Anote seu controller \ resource:

```java
@RestController
@RequestMapping("/api/example")
@KelariGenerateApiTest(
        authUrl = "/api/auth/login",
        username = "admin",
        password = "123456",
        parameterTokenName = "token" // o default é "token"
)
public class ExampleResource {
    @ApiTestSpec(
            scenarios = {
                    @ApiTestCase(
                            displayName = "✅ Should return 200 OK when 'id' is 200 and authenticated",
                            order = 1,
                            timeout = 5,
                            expectedStatusCode = HttpURLConnection.HTTP_OK,
                            dataProviderClassName = "com.example.demo.data.GetExampleDataLoad200",
                            requiresAuth = true,
                            repeat = 5,
                            enableLogging = true,
                            responseTimeoutSeconds = 5,
                            expectedHeaders = {@Header(name = "testName1", value = {"testValue1", "testValue11"}),
                                    @Header(name = "testName2", value = "testValue2")},
                            jsonPaths = {
                                    // EQUAL_TO: o valor retornado deve ser exatamente "John"
                                    @JsonPath(path = "$.name", type = MatcherType.EQUAL_TO, value = "John"),
                                    // NULL_VALUE: espera que o valor seja null
                                    @JsonPath(path = "$.optionalField", type = MatcherType.NULL_VALUE),
                                    // NOT_NULL_VALUE: espera que o campo não seja null
                                    @JsonPath(path = "$.mandatoryField", type = MatcherType.NOT_NULL_VALUE),
                                    // NOT: nega o valor (ex: não deve ser "Admin")
                                    @JsonPath(path = "$.role", type = MatcherType.NOT, value = "Admin"),
                                    // INSTANCE_OF: espera que o valor seja do tipo Integer
                                    @JsonPath(path = "$.age", type = MatcherType.INSTANCE_OF, value = "java.lang.Integer"),
                                    // GREATER_THAN: espera que o valor seja maior que 10
                                    @JsonPath(path = "$.score", type = MatcherType.GREATER_THAN, value = "10"),
                                    // LESS_THAN: espera que o valor seja menor que 100
                                    @JsonPath(path = "$.limit", type = MatcherType.LESS_THAN, value = "100"),
                                    // CONTAINS_STRING: espera que o valor contenha a substring "success"
                                    @JsonPath(path = "$.message", type = MatcherType.CONTAINS_STRING, value = "success"),
                                    // STARTS_WITH: espera que o valor comece com "user_"
                                    @JsonPath(path = "$.username", type = MatcherType.STARTS_WITH, value = "user_"),
                                    // ENDS_WITH: espera que o valor termine com ".com"
                                    @JsonPath(path = "$.email", type = MatcherType.ENDS_WITH, value = ".com"),
                                    // ANY_OF: espera que o valor seja qualquer um da lista ["A", "B", "C"]
                                    @JsonPath(path = "$.grade", type = MatcherType.ANY_OF, value = "A,B,C"),
                                    // CONTAINS: espera que a lista contenha "admin"
                                    @JsonPath(path = "$.roles", type = MatcherType.HAS_ITEM, value = "admin"),
                                    // CUSTOM_CLASS: matcher Java personalizado
                                    @JsonPath(path = "$.name", type = MatcherType.CUSTOM_CLASS, matcherClass = IsJohnMatcher.class)
                            }
                    ),
                    @ApiTestCase(
                            displayName = "❌ Should return 400 Bad Request when 'id' is 400",
                            order = 2,
                            timeout = 5,
                            expectedStatusCode = HttpURLConnection.HTTP_BAD_REQUEST,
                            dataProviderClassName = "com.example.demo.data.GetExampleDataLoad400",
                            requiresAuth = true
                    ),
                    @ApiTestCase(
                            displayName = "🛡️ Should return 401 Unauthorized when Authorization header is missing",
                            order = 3,
                            timeout = 3,
                            expectedStatusCode = HttpURLConnection.HTTP_UNAUTHORIZED,
                            dataProviderClassName = "com.example.demo.data.GetExampleDataLoad401",
                            requiresAuth = false
                    ),
                    @ApiTestCase(
                            displayName = "❌ Should return 404 Not Found when 'id' is 404",
                            order = 4,
                            timeout = 5,
                            expectedStatusCode = HttpURLConnection.HTTP_NOT_FOUND,
                            dataProviderClassName = "com.example.demo.data.GetExampleDataLoad404",
                            requiresAuth = true
                    ),
                    @ApiTestCase(
                            displayName = "❌ Should return 500 Internal Server Error for unhandled 'id'",
                            order = 5,
                            timeout = 5,
                            expectedStatusCode = HttpURLConnection.HTTP_INTERNAL_ERROR,
                            dataProviderClassName = "com.example.demo.data.GetExampleDataLoad500",
                            requiresAuth = true
                    )
            }
    )
    // GET com @PathVariable e @RequestParam
    @GetMapping("/{id}")
    public ResponseEntity<DataResponse> getExample(
            @PathVariable("id") Long id,
            @RequestParam(required = false) String filter,
            @RequestHeader(value = "X-Custom-Header", required = false) String customHeader){...}
}
```

### 2. Implemente o `DataLoad` (opcional):

```java
public class GetExampleDataLoad200 implements DataLoad {
    @Override
    public Map<String, Object> load() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1L);
        data.put("filter", "test");
        data.put("X-Custom-Header", "abc-123");
        return data;
    }
}
```

### 3. 🔧 Customização de Matchers

O Kelari permite o uso de **Matchers personalizados** com Hamcrest, possibilitando validações específicas que vão além dos matchers padrão.

Essa abordagem é útil quando se deseja expressar lógicas mais específicas ou reutilizar regras de validação complexas em diferentes testes.

### 📌 Exemplo: Matcher personalizado `IsJohnMatcher`

```java
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class IsJohnMatcher extends BaseMatcher<String> {

    @Override
    public boolean matches(Object item) {
        return "John".equals(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a string equal to 'John'");
    }
}

✅ Utilização no Kelari

// CUSTOM_CLASS: matcher Java personalizado
@JsonPath(path = "$.name", type = MatcherType.CUSTOM_CLASS, matcherClass = IsJohnMatcher.class)

ℹ️ Importante: Certifique-se de que a classe do matcher esteja disponível no classpath do projeto onde os testes gerados são executados.

```
### 4. Resultado Gerado (exemplo de teste):

```java
@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
public class ExampleResourceGeneratedTest {
    @Autowired
    private WebTestClient webTestClient;

    private String bearerToken = "";

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            System.out.println("[REQUEST] → " + request.method() + " " + request.url());
            request.headers().forEach((k, v) -> System.out.println("[HEADER] " + k + ": " + v));
            request.cookies().forEach((k, v) -> System.out.println("[COOKIE] " + k + ": " + v));
            // [BODY] Logging not implemented. Next release.
            return Mono.just(request);
        });
    }

    private static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            System.out.println("[RESPONSE] ← Status: " + response.statusCode());
            response.headers().asHttpHeaders()
                    .forEach((k, v) -> System.out.println("[HEADER] " + k + ": " + v));
            return Mono.just(response);
        });
    }

    @BeforeEach
    public void authenticate() {
        if ("/api/auth/login".isEmpty()) {
            return;
        }
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "admin");
        webTestClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(credentials), Map.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.token")
                .value(token -> bearerToken = "Bearer " + token);
    }

    @RepeatedTest(5)
    @Timeout(5)
    @Order(1)
    @DisplayName("✅ Should return 200 OK when 'id' is 200 and authenticated")
    public void getExample_200() {
        WebTestClient client = this.webTestClient
                .mutate()
                .filter(logRequest())
                .filter(logResponse())
                .responseTimeout(Duration.ofSeconds(5))
                .build();
        Map<String, Object> data = getData("com.example.demo.data.GetExampleDataLoad200");
        client
                .get()
                .uri("/api/example/" + safeString(data.get("id"))  + "?filter=" + safeString(data.get("filter")))
                .header("X-Custom-Header", safeString(data.get("X-Custom-Header")))
                .header("Authorization", bearerToken)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("testName2", "testValue2")
                .expectHeader().valueEquals("testName1", "testValue1", "testValue11")
                .expectBody()
                .jsonPath("$.name").value(Matchers.equalTo("John"))
                .jsonPath("$.mandatoryField").value(Matchers.notNullValue())
                .jsonPath("$.age").value(Matchers.instanceOf(Integer.class))
                .jsonPath("$.score").value(Matchers.greaterThan(10))
                .jsonPath("$.limit").value(Matchers.lessThan(100))
                .jsonPath("$.optionalField").value(Matchers.nullValue())
                .jsonPath("$.message").value(Matchers.containsString("success"))
                .jsonPath("$.name").value(new IsJohnMatcher())
                .jsonPath("$.username").value(Matchers.startsWith("user_"))
                .jsonPath("$.grade").value(Matchers.anyOf(Matchers.equalTo("A"), Matchers.equalTo("B"), Matchers.equalTo("C")))
                .jsonPath("$.role").value(Matchers.not(Matchers.equalTo("Admin")))
                .jsonPath("$.email").value(Matchers.endsWith(".com"))
                .jsonPath("$.roles").value(Matchers.hasItem("admin"));
    }

    @Test
    @Timeout(5)
    @Order(2)
    @DisplayName("❌ Should return 400 Bad Request when 'id' is 400")
    public void getExample_400() {
        Map<String, Object> data = getData("com.example.demo.data.GetExampleDataLoad400");
        webTestClient
                .get()
                .uri("/api/example/" + safeString(data.get("id"))  + "?filter=" + safeString(data.get("filter")))
                .header("X-Custom-Header", safeString(data.get("X-Custom-Header")))
                .header("Authorization", bearerToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Timeout(3)
    @Order(3)
    @DisplayName("🛡️ Should return 401 Unauthorized when Authorization header is missing")
    public void getExample_401() {
        Map<String, Object> data = getData("com.example.demo.data.GetExampleDataLoad401");
        webTestClient
                .get()
                .uri("/api/example/" + safeString(data.get("id"))  + "?filter=" + safeString(data.get("filter")))
                .header("X-Custom-Header", safeString(data.get("X-Custom-Header")))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @Timeout(5)
    @Order(4)
    @DisplayName("❌ Should return 404 Not Found when 'id' is 404")
    public void getExample_404() {
        Map<String, Object> data = getData("com.example.demo.data.GetExampleDataLoad404");
        webTestClient
                .get()
                .uri("/api/example/" + safeString(data.get("id"))  + "?filter=" + safeString(data.get("filter")))
                .header("X-Custom-Header", safeString(data.get("X-Custom-Header")))
                .header("Authorization", bearerToken)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Timeout(5)
    @Order(5)
    @DisplayName("❌ Should return 500 Internal Server Error for unhandled 'id'")
    public void getExample_500() {
        Map<String, Object> data = getData("com.example.demo.data.GetExampleDataLoad500");
        webTestClient
                .get()
                .uri("/api/example/" + safeString(data.get("id"))  + "?filter=" + safeString(data.get("filter")))
                .header("X-Custom-Header", safeString(data.get("X-Custom-Header")))
                .header("Authorization", bearerToken)
                .exchange()
                .expectStatus().is5xxServerError();
    }

}
```

---

## 📄 Anotações

### `@KelariGenerateApiTest`

Anotação de nível de classe que ativa a geração automática de testes para um controller.

| Atributo              | Tipo     | Descrição                                                                                         |
|-----------------------|----------|---------------------------------------------------------------------------------------------------|
| `authUrl`             | `String` | URL para autenticação antes dos testes (ex: `/auth/token`)                                       |
| `username`            | `String` | Nome de usuário usado na autenticação                                                             |
| `password`            | `String` | Senha usada na autenticação                                                                       |
| `parameterTokenName`  | `String` | Nome do campo no corpo da resposta JSON que contém o token (ex: `"token"` ou `"access_token"`)   |

> 🔐 Quando combinada com `@ApiTestCase(requiresAuth = true)`, o token extraído via `jsonPath("$.<parameterTokenName>")` será adicionado automaticamente ao header `Authorization` dos testes.

### `@ApiTestSpec`

| Atributo     | Tipo                 | Descrição                                                                 |
|--------------|----------------------|---------------------------------------------------------------------------|
| `scenarios`  | `ApiTestCase[]`      | Lista de cenários que descrevem os testes para o endpoint                 |

### `@ApiTestCase`

| Atributo                | Tipo                        | Descrição                                                                   |
|-------------------------|-----------------------------|------------------------------------------------------------------------------|
| `displayName`           | `String`                    | Nome descritivo do teste, usado em relatórios, logs ou documentação gerada. |
| `order`                 | `int`                       | Ordem de execução do teste, útil para organizar a sequência dos testes.     |
| `timeout`               | `int`                       | Tempo máximo de execução do teste (em segundos). Se excedido, o teste falha.|
| `expectedStatusCode`    | `int`                       | Código HTTP esperado na resposta da API.                                    |
| `dataProviderClassName` | `String[]`                  | Classe que implementa `DataLoad` para fornecer dados ao teste (opcional).  |
| `requiresAuth`          | `boolean`                   | Indica se o teste requer autenticação. Quando `true`, cabeçalhos de `Authorization` são incluídos.|
| `jsonPaths`             | `JsonPath[]`                | Lista de expressões JSONPath e seus valores esperados para validar na resposta da API. |
| `enableLogging`         | `boolean`                   | Ativa ou desativa o log para o caso de teste. Útil para depuração.          |
| `expectedHeaders`       | `Header[]`                  | Lista de cabeçalhos HTTP esperados na resposta.                             |
| `expectedCookies`       | `Cookie[]`                  | Lista de cookies HTTP esperados na resposta.                               |
| `repeat`                | `int`                       | Número de vezes que o teste será executado consecutivamente.                |
| `responseTimeoutSeconds`| `long`                      | Tempo máximo de espera pela resposta, em segundos. Override do timeout global, se especificado. |


> O Gerador de Testes Kelari suporta endpoints protegidos usando OAuth2/JWT. Basta definir requiresAuth = true em @ApiTestCase e o token será injetado automaticamente.
```java
webTestClient
    .post()
    .uri("/api/example/upload")
    .header("Authorization", bearerToken) // automatic!
    ...
```
---

## 📦 Projeto de Exemplo

Deseja ver um exemplo prático de como utilizar o **Kelari API Test Generator** em uma aplicação Spring Boot?

➡️ Acesse o repositório oficial de exemplo:

**🔗 [kelari-spring-api-test-generator-sample](https://github.com/agsn10/kelari-spring-api-test-generator-sample)**
> Um projeto demonstrando como integrar o Kelari com controladores Spring e gerar testes automatizados para os endpoints.
---
## 🔍 Tabela Comparativa

A tabela abaixo apresenta uma comparação entre o **Kelari**, o Rest-Assured e o Postman/Newman, destacando os recursos disponíveis em cada ferramenta para testes automatizados de APIs REST:

| Recurso / Ferramenta | **Kelari** ✅ | Rest-Assured ⚙️ | Postman/Newman 🌐 |
|----------------------|---------------|------------------|-------------------|
| Linguagem            | Java          | Java             | JavaScript (CLI)  |
| Geração Automática de Testes | ✅ Sim     | ❌ Manual         | ✅ Sim (mas não nativo em Java) |
| Nativo para Spring Boot | ✅ Sim     | ✅ Sim            | ❌ Não             |
| Suporte a JSON Body  | ✅ Sim         | ✅ Sim            | ✅ Sim             |
| Suporte a Upload de Arquivo | ✅ Sim     | ⚠️ Parcial        | ✅ Sim             |
| Suporte a JWT/OAuth2 | ✅ Automático | ✅ Manual         | ✅ Manual          |
| Suporte a Path/Query/Header/Cookie Params | ✅ Sim | ✅ Sim | ✅ Sim |
| Extensível (estratégias personalizadas) | ✅ Sim | ✅ Sim | ❌ Não |
| Integração com JUnit 5 | ✅ Nativa     | ✅ Nativa         | ❌ Não             |
| Curva de Aprendizado | 🟢 Baixa       | 🟡 Média          | 🔴 Alta (para devs Java) |
| Requer OpenAPI?      | ❌ Não         | ❌ Não            | ⚠️ Recomendado     |
| Suporte a CLI/Maven  | ✅ Maven       | ❌ Não            | ✅ CLI             |

> ⚠️ *O Rest-Assured permite upload de arquivos, mas requer configuração manual com multipart. No Kelari, isso é feito automaticamente usando `@ModelAttribute` e `@RequestPart`.*


---
## ✅ Porquê usar?

### 1. Redução de Boilplate e Tempo de Desenvolvimento

Gerar testes automaticamente com base em anotações (`@KelariGenerateApiTest`, `@ApiTestCase` e `@ApiTestSpec`) é uma solução extremamente prática. Evita-se escrever centenas de linhas de código repetitivo e propenso a erros.

### 2. Legibilidade e Intenção Clara

Os cenários com `displayName`, `expectedStatusCodes`, `order` e `timeout` são autoexplicativos. Isso comunica intenção e facilita revisões de código e documentação viva dos testes.

### 3. Foco em Qualidade e Cobertura

Incentiva os desenvolvedores a pensarem nos casos de sucesso e falha da API desde o momento da implementação — isso vai ao encontro de práticas como TDD e Design by Contract, que especifica o que se espera que um método faça e o que garante que ele fará, facilitando a validação e a depuração do código.

### 4. Integração Transparente

A ideia do Kelari se baseia na Javac Compiler API, explorando a árvore sintática abstrata (AST) do código-fonte via com.sun.source.util.Plugin, oferecendo geração de testes baseada em análise estrutural profunda do código Java. É elegante e não intrusiva. Não força alterações estruturais na aplicação nem depende de código-fonte externo.

### 5. Melhora a Confiabilidade

Ao definir claramente as obrigações de cada parte, o Design by Contract ajuda a identificar e corrigir erros mais cedo no processo de desenvolvimento.

### 6. Facilita a Depuração

A definição explícita dos contratos permite que desenvolvedores usem o Design by Contract para entender melhor o comportamento do código e identificar a causa de falhas.

### 7. Melhora a Documentação

Os contratos podem ser usados como documentação formal do sistema, facilitando a comunicação entre os desenvolvedores.


> 💡 “O que está sendo testado reflete o que está documentado”.

---

# 🤝 Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir *issues*, enviar *pull requests* ou sugerir melhorias.

---

## ☕ Pague-me um café, apoie a iniciativa!
Projetado para desenvolvedores que valorizam produtividade, qualidade e automação, o Kelari analisa seu código e produz casos de teste significativos com apenas alguns comandos e em segundos. Seja usando Spring Boot, o Kelari ajuda você a economizar horas escrevendo testes boilerplate – para que você possa se concentrar no que realmente importa: construir um software de qualidade. Se você gostou do projeto ou se o projeto agrega qualidade em suas entregas e reduzindo custos em seus projetos. Se você deseja apoiar o desenvolvimento, me ajude a continuar criando mais funcionalidades, corrigindo bugs e mantendo o código. Você pode fazer isso através do meu PIX:
agsn10@hotmail.com.


---

## 📜 Licença

Distribuído sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---