# Conecta

🚀 Conecta
Conecta é uma plataforma de marketplace de serviços locais, projetada para ser a ponte direta entre clientes que buscam serviços e os prestadores de serviço de uma região.

🎯 Sobre o Projeto
O Conecta foi desenhado para ser um hub central de serviços. Ele combina um sistema de busca por categorias (similar a um "Mercado Livre de Serviços") com um portfólio visual (similar ao "Instagram"), onde os prestadores podem exibir seus trabalhos (Publicações) e os clientes podem deixar suas opiniões (Avaliações).

Funcionalidades Atuais
Segurança Completa: Autenticação de ponta-a-ponta com Spring Security e Token JWT.

Criptografia: Senhas são armazenadas de forma segura usando BCrypt.

Autorização por Papel (Roles): A API distingue ROLE_CLIENTE de ROLE_PRESTADOR, garantindo que:

Apenas Prestadores podem criar publicações.

Apenas Clientes podem deixar avaliações.

Apenas o "dono" de um item (publicação ou avaliação) pode editá-lo ou deletá-lo.

CRUD Completo:

Usuários: Cadastro (POST) e Edição de Perfil (PUT) para Clientes e Prestadores.

Publicações: CRUD completo (Create, Read, Update, Delete) para o portfólio do Prestador.

Avaliações: CRUD completo (Create, Read, Update, Delete) para avaliações de Clientes.

Frontend Reativo: Interface construída em Angular que reage dinamicamente ao estado de login e ao papel do usuário (ex: mostrando/escondendo botões e formulários).

Validação: O backend valida os dados de entrada (ex: emails, senhas) e o frontend possui formulários reativos com validação em tempo real.

🛠️ Pilha Tecnológica (Tech Stack)
Backend (Spring Boot)
Java 24

Spring Boot 3

Spring Security 6 (Autenticação JWT & Autorização por Papel)

Spring Data JPA (Hibernate)

Maven

H2 Database (Banco de dados em memória para desenvolvimento)

Frontend (Angular)
Angular 17+

TypeScript

Angular Reactive Forms

CSS Moderno (com layout de Grid e Flexbox)

🏁 Como Inicializar o Projeto (Getting Started)
Siga estes passos para rodar o projeto em uma nova máquina.

1. Pré-requisitos
   Antes de começar, garanta que você tenha as seguintes ferramentas instaladas:

Git (para clonar o repositório).

Java JDK 24 (ou superior).

Node.js (LTS) (que inclui o npm).

Angular CLI (instale globalmente rodando: npm install -g @angular/cli).

2. Clonar o Repositório

# Entre na pasta principal do projeto

cd Conecta

3. Inicializar o Backend (Spring Boot)
   Abra a pasta Conecta no VS Code.

Aguarde alguns segundos. A extensão Java do VS Code irá detectar o projeto backend e começar a sincronizar e baixar as dependências do Maven (do arquivo backend/pom.xml).

Quando a sincronização terminar, abra o arquivo principal: backend/src/main/java/br/com/conecta/DemoApplication.java

Acima do método main, clique no botão "Run".

Aguarde o terminal do VS Code exibir a mensagem: Started DemoApplication...

Parabéns, seu backend está rodando em http://localhost:8080!

4. Inicializar o Frontend (Angular)
   O backend DEVE estar rodando para o frontend funcionar.

Abra um novo terminal no VS Code (Ctrl+' ou Terminal > New Terminal).

Navegue até a pasta do frontend:

Bash

cd frontend
Instale todas as dependências do Angular (isso só é feito na primeira vez):

Bash

npm install
Inicie o servidor de desenvolvimento (usando npx para garantir que ele use a versão local):

Bash

npx ng serve
Acesse a aplicação no seu navegador: http://localhost:4200

🧪 Fluxo de Teste Recomendado
Como o banco de dados H2 é em memória (zerado a cada reinício), siga este fluxo para testar:

Inicie o Backend e o Frontend.

Crie Categorias:

Use o Postman para cadastrar algumas categorias "fixas".

POST para http://localhost:8080/api/categorias

Body (JSON): {"nome": "Marcenaria", "descricao": "..."}

Repita para "Pintura", "Elétrica", etc.

Crie Usuários:

No frontend (localhost:4200), clique em "Cadastrar Cliente" e crie um cliente.

Clique em "Cadastrar Prestador", preencha o formulário (os checkboxes de categoria agora devem aparecer).

Teste o Login e as Funções:

Faça Login como o Cliente que você criou.

Navegue até o perfil de um Prestador.

Teste (Cliente): O formulário de Publicação deve estar escondido. O formulário de Avaliação deve estar visível. Crie e delete uma avaliação.

Faça Logout.

Faça Login como o Prestador que você criou.

Navegue até o seu próprio perfil (ex: .../prestador/1).

Teste (Prestador): O formulário de Avaliação deve estar escondido. O formulário de Publicação deve estar visível. Crie, edite e delete uma publicação.
