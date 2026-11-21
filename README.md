# 🌤️ Clima API — Backend

API REST em Java/Spring Boot do projeto **App de Clima Detalhado**, projeto acadêmico de 4 entregas. Ela consulta o clima na API do OpenWeatherMap, guarda histórico e favoritos no MySQL e protege tudo com autenticação JWT.

O frontend (React + TypeScript) está em outro repositório: [`frontend-clima`](https://github.com/Projeto-Final-ADS-2026/frontend-clima).

## 📋 Entregas

| Entrega | Tema | Status |
|---|---|---|
| 1 | Interface visual com dados mockados (frontend) | ✅ |
| 2 | Backend com API real | ✅ |
| 3 | Banco de dados, histórico e favoritos | ✅ |
| 4 | Funcionalidades avançadas | 🚧 Só a autenticação JWT foi feita |

### O que cada entrega trouxe neste repositório
- **Entrega 2:** integração com o OpenWeatherMap, endpoint de clima por cidade, CORS e documentação Swagger.
- **Entrega 3:** entidades JPA (`City`, `Favorite`, `WeatherHistory`), histórico de buscas, favoritos e pontos de tendência de temperatura, persistidos em MySQL.
- **Entrega 4:** cadastro, login e rotas protegidas com JWT; favoritos e histórico passam a ser **por usuário**.

### Entrega 4: o que ficou de fora
Autenticação JWT é a única parte da Entrega 4 implementada até aqui. **Ainda não existem:** alertas customizados, comparação entre cidades, notificações, dashboard personalizado e deploy em produção.

## 🚀 Tecnologias
- Java 21
- Spring Boot 4.1.1 (Web MVC, Data JPA, Security, Validation)
- MySQL 8
- JWT com [jjwt](https://github.com/jwtk/jjwt) 0.12.6
- Springdoc OpenAPI (Swagger UI) 3.1.0
- Lombok
- Maven (wrapper incluso)

## 📦 Como rodar

### Pré-requisitos
- JDK 21 ou superior
- MySQL rodando em `localhost:3306` (o banco `weather_db` é criado automaticamente)
- Uma chave da API do [OpenWeatherMap](https://openweathermap.org/api)

### Configuração
As credenciais **não ficam no repositório**. Crie o arquivo `src/main/resources/application-local.properties` (já está no `.gitignore`):

```properties
weather.api.key=SUA_CHAVE_DO_OPENWEATHER
jwt.secret=UMA_FRASE_ALEATORIA_COM_PELO_MENOS_32_CARACTERES
spring.datasource.username=SEU_USUARIO_MYSQL
spring.datasource.password=SUA_SENHA_MYSQL
```

Em vez do arquivo, você pode usar variáveis de ambiente:

| Variável | Obrigatória | Descrição |
|---|---|---|
| `WEATHER_API_KEY` | sim | Chave do OpenWeatherMap |
| `JWT_SECRET` | sim | Segredo para assinar os tokens (mínimo 32 caracteres) |
| `CORS_ALLOWED_ORIGINS` | não | Origens do frontend, separadas por vírgula (ex.: `http://localhost:3000`) |

> ⚠️ Nunca faça commit de chaves ou senhas. Se o `jwt.secret` mudar, todos os tokens emitidos deixam de valer e os usuários precisam entrar de novo.

### Subindo a aplicação
```bash
# usando o arquivo application-local.properties
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```
A API fica em `http://localhost:8080`. A documentação interativa fica em `http://localhost:8080/swagger-ui.html`.

### Testes
```bash
./mvnw test
```
Os testes usam um banco H2 em memória, então não precisam de MySQL nem de chave do OpenWeatherMap. Eles cobrem cadastro, login, `/me`, e-mail duplicado, validação de senha e acesso sem token.

## 🔐 Autenticação
1. `POST /api/auth/register` ou `POST /api/auth/login` devolvem `{ "token": "...", "user": {...} }`.
2. Nas demais chamadas, envie o header `Authorization: Bearer <token>`.
3. O token expira em 24 horas (`jwt.expiration-ms`).
4. Senhas são guardadas com BCrypt. A API é stateless, sem sessão no servidor.

Rotas públicas: `/api/auth/register`, `/api/auth/login`, Swagger e `/v3/api-docs`. Todo o resto de `/api/**` exige token.

## 🌐 Endpoints

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| POST | `/api/auth/register` | não | Cria conta (`name`, `email`, `password` com 6+ caracteres) |
| POST | `/api/auth/login` | não | Entra com `email` e `password` |
| GET | `/api/auth/me` | sim | Dados do usuário logado |
| GET | `/api/clima/{cidade}` | sim | Clima atual da cidade e grava no histórico do usuário |
| GET | `/api/historico?limite=10` | sim | Últimas buscas do usuário |
| GET | `/api/historico/tendencia/{cidade}?limite=20` | sim | Pontos de temperatura para o gráfico de tendência |
| DELETE | `/api/historico` | sim | Limpa o histórico do usuário |
| GET | `/api/favoritos` | sim | Lista os favoritos do usuário |
| POST | `/api/favoritos` | sim | Adiciona favorito (`{ "cityName", "country" }`) |
| DELETE | `/api/favoritos/{id}` | sim | Remove um favorito do próprio usuário |

Favoritar uma cidade já favoritada devolve o favorito existente. Um usuário não consegue ver nem apagar favoritos ou histórico de outro.

## 🗄️ Banco de dados
O schema é criado e atualizado pelo Hibernate (`ddl-auto=update`).

| Tabela | Conteúdo |
|---|---|
| `users` | Usuários (e-mail único e senha com hash) |
| `cities` | Cidades (nome + país únicos) |
| `favorites` | Favoritos, únicos por (usuário, cidade) |
| `weather_history` | Histórico de consultas, por usuário |

### Migrando um banco criado antes da autenticação
O `ddl-auto=update` adiciona colunas e índices novos, mas **não remove o índice único antigo** de `favorites` em `city_id`. Enquanto ele existir, dois usuários não conseguem favoritar a mesma cidade. Para corrigir:

```sql
USE weather_db;
SHOW INDEX FROM favorites;  -- ache o índice único que tem só city_id
ALTER TABLE favorites ADD INDEX idx_favorites_city (city_id);
ALTER TABLE favorites DROP INDEX <nome_do_indice_unico_antigo>;
DELETE FROM favorites WHERE user_id IS NULL;
```

O índice comum em `city_id` precisa ser criado antes, porque a chave estrangeira depende de um índice nessa coluna. Favoritos e histórico anteriores à autenticação ficam sem dono (`user_id` nulo) e não aparecem para ninguém.

## 📂 Estrutura
```
src/main/java/backend_clima/dev/
├── config/        # SecurityConfig, WebConfig (CORS), RestClientConfig
├── controller/    # Auth, Weather, Favorite, History
├── service/       # AuthService, WeatherService, FavoriteService, HistoryService, CityService
├── security/      # JwtService e JwtAuthFilter
├── entity/        # User, City, Favorite, WeatherHistory
├── repository/    # Repositórios Spring Data JPA
└── dto/           # Objetos de entrada e saída da API
```

## 📝 Limitações conhecidas
- O nome da cidade é concatenado na URL do OpenWeatherMap sem codificação.
- A previsão de 5 dias exibida no frontend ainda é simulada; a API só fornece o clima atual.
- Documentação do Swagger e logs de SQL estão ligados, o que é adequado para desenvolvimento, não para produção.

## 👨‍💻 Autor
Hiago — projeto acadêmico da Faculdade Impacta.

## 📄 Licença
MIT
