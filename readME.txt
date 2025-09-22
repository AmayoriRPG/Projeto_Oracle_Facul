Sistema de Gestão de Projetos e Equipes
Este documento detalha o projeto de um sistema web para gestão de projetos e equipes, desenvolvido como uma solução para controlar o andamento de múltiplos projetos, alocação de colaboradores e geração de relatórios gerenciais.

1. Objetivo
O principal objetivo deste sistema é fornecer uma ferramenta centralizada e eficiente para o controle de projetos, cujo desenvolvimento é de responsabilidade de equipes multidisciplinares (desenvolvedores, analistas, designers). A plataforma visa garantir o cumprimento de prazos, otimizar a alocação de profissionais e facilitar o acompanhamento do status de cada projeto, desde o planejamento até a conclusão.

2. Funcionalidades Implementadas
O sistema conta com as seguintes funcionalidades:

Autenticação e Controle de Acesso:

Sistema de login seguro com controle de sessão.

Níveis de acesso baseados em perfis de usuário: ADMINISTRADOR, GERENTE e COLABORADOR.

Gestão de Usuários (CRUD - Apenas Administradores):

Cadastro, listagem, edição e exclusão de usuários do sistema.

Gestão de Equipes (CRUD):

Cadastro, listagem, edição e exclusão de equipes de trabalho.

Visualização detalhada de cada equipe.

Gestão de Projetos (CRUD):

Cadastro, listagem, edição e exclusão de projetos.

Definição de nome, descrição, datas de início/término, status e gerente responsável.

Visualização detalhada de cada projeto.

Alocação de Recursos:

Membros em Equipes: Adição e remoção de usuários (colaboradores) em equipes específicas.

Equipes em Projetos: Alocação e desalocação de equipes inteiras para trabalhar em projetos específicos.

Visualização Cruzada: A página de detalhes de um projeto mostra as equipes alocadas, e a página de detalhes de uma equipe mostra os projetos que ela atende.

Relatórios Gerenciais:

Página de relatórios com um gráfico de pizza (Pie Chart) que exibe a distribuição de projetos por status (PLANEJADO, EM_ANDAMENTO, CONCLUIDO, CANCELADO).

Funcionalidade para exportar a lista completa de projetos para uma planilha do Excel (.xlsx).

3. Requisitos e Tecnologias Utilizadas
Pré-requisitos de Software
Java Development Kit (JDK): Versão 21 ou superior.

Apache Maven: Versão 3.8 ou superior.

Apache Tomcat: Versão 10.1 ou superior (compatível com Jakarta EE 10).

Oracle Database: Versão 12c ou superior (com arquitetura Multitenant - CDB/PDB).

IDE: Visual Studio Code com o "Extension Pack for Java".

Cliente SQL: SQL Developer, DBeaver ou similar para gerenciamento do banco de dados.

Tecnologias e Bibliotecas
Back-end:

Java

Jakarta Servlets 5.0

Jakarta Server Pages (JSP) 3.0

JSTL (Jakarta Standard Tag Library) 2.0

JDBC (para conexão com o Oracle DB)

Banco de Dados: Oracle Database

Build e Dependências: Apache Maven

Bibliotecas Externas:

Apache POI: Para geração de arquivos Excel (.xlsx).

Front-end:

HTML5 / CSS3

Bootstrap 5 (para o layout responsivo)

Chart.js: Para a renderização de gráficos na página de relatórios.

4. Instruções de Execução
Siga os passos abaixo para configurar e executar o projeto em um ambiente local.

Passo 1: Configuração do Banco de Dados
Conecte-se ao seu Oracle Database como um usuário administrador (ex: SYSTEM).

Crie um Pluggable Database (PDB) ou utilize um existente (ex: XEPDB1, FREEPDB1).

Execute o script para criar um usuário local para a aplicação dentro do PDB desejado. Lembre-se de alterar a senha e o nome do PDB no script.

SQL

ALTER SESSION SET CONTAINER = XEPDB1;
CREATE USER projeto IDENTIFIED BY a1234;
GRANT CONNECT, RESOURCE TO projeto;
ALTER USER projeto ACCOUNT UNLOCK;
Conecte-se ao banco de dados com o novo usuário projeto.

Execute o script SQL fornecido no projeto (scripts/create_tables.sql) para criar todas as tabelas e inserir os dados iniciais.

Passo 2: Configuração do Projeto
Clone este repositório para sua máquina local.

Abra a pasta do projeto no Visual Studio Code.

Navegue até o arquivo src/main/java/com/projeto_oracle/dao/Conexao.java.

Altere as variáveis URL, USUARIO e SENHA para que correspondam à sua configuração do banco de dados Oracle, incluindo o Service Name do seu PDB.

Passo 3: Build do Projeto
Abra a visão MAVEN na barra lateral do VS Code.

Execute o ciclo de vida clean para limpar compilações anteriores.

Execute o ciclo de vida install para compilar o código e empacotar a aplicação em um arquivo .war.

Passo 4: Execução no Servidor Tomcat
Configure um servidor Apache Tomcat 10.1+ no VS Code através da extensão "Community Server Connectors".

Na visão SERVERS, clique com o botão direito no servidor configurado e clique em "Start".

Na visão MAVEN, clique com o botão direito no projeto e selecione "Run on Server...", escolhendo o servidor que você acabou de iniciar.

Passo 5: Acesso à Aplicação
Após o deploy bem-sucedido, a aplicação estará disponível no seu navegador.

Acesse a URL principal: http://localhost:8080/projeto/

Utilize as credenciais de um dos usuários padrão inseridos pelo script SQL para fazer o login (ex: admin / senha_segura_admin).

5. Autor
Desenvolvido por Victor Lopes.