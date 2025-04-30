# 📦 CI com Gradle, OWASP Dependency Check e Trivy

Este repositório contém uma aplicação Spring Boot com um pipeline de integração contínua configurado via **GitHub Actions**. O objetivo é:

- Construir o projeto com Gradle
- Avaliar vulnerabilidades em dependências com **OWASP Dependency Check**
- Gerar imagem Docker da aplicação
- Escanear essa imagem com **Trivy**
- Publicar relatórios de segurança como artefatos

---

## 🚀 Pipeline: `.github/workflows/ci.yml`

### 🔄 Quando é executado?

Este pipeline roda automaticamente nos eventos:
- `push` no branch `main`
- `pull_request` direcionado ao `main`

---

## 🔍 Etapas do workflow (job `build`)

### 🧾 `runs-on: ubuntu-22.04`
Define o sistema operacional da máquina virtual usada pelo GitHub Actions. Usamos Ubuntu 22.04 por ser estável e compatível com todas as actions utilizadas.

---

### ✅ Etapas detalhadas

#### 1. **Checkout do código**
Clona o código fonte do repositório para o ambiente de execução.

#### 2. **Setup do Java 17**
Configura o Java 17 (distribuição Temurin) e ativa o cache para builds Gradle.

#### 3. **Permissão ao Gradle Wrapper**
Garante que o wrapper (`gradlew`) tenha permissão de execução no Linux.

#### 4. **Build com Gradle (sem testes)**
Compila o projeto e empacota o `.jar`, ignorando os testes.

#### 5. **Instalar dependências de sistema**
Instala ferramentas necessárias para baixar e extrair o OWASP Dependency Check.

#### 6. **Instalar OWASP Dependency Check**
Faz o download da ferramenta OWASP Dependency Check versão 8.3.1, que identifica vulnerabilidades conhecidas nas dependências do projeto.

#### 7. **Executar análise de dependências**
Roda o scan de segurança nas dependências do projeto e gera um relatório em HTML.

> ⚠️ A pipeline **não falha mesmo que existam vulnerabilidades**. Isso é intencional e controlado com `|| echo`.

#### 8. **Verificar se o relatório foi gerado**
Confirma que o relatório HTML foi realmente gerado. Caso contrário, a pipeline falha.

#### 9. **Upload do relatório OWASP**
Envia o relatório HTML e o log da análise como **artefato do GitHub Actions**, acessível na aba "Actions".

#### 10. **Build da imagem Docker**
Cria a imagem Docker com o nome `demo-git-actions`, usando o Dockerfile na raiz do projeto.

#### 11. **Scan da imagem com Trivy**
Executa o scanner Trivy para verificar se há vulnerabilidades **CRÍTICAS ou ALTAS** na imagem Docker.

> ⚠️ O parâmetro `exit-code: 0` impede que o pipeline falhe, mesmo se vulnerabilidades forem detectadas.

#### 12. **Exportar relatório Trivy**
Executa o scan novamente e exporta os resultados para um arquivo `.txt`.

#### 13. **Upload do relatório Trivy**
Disponibiliza o relatório do Trivy como artefato na aba "Actions" do GitHub.

---

## 📁 Artefatos gerados

Ao final da execução, dois arquivos estarão disponíveis para download:
- `dependency-check-report.html`: vulnerabilidades nas dependências do código.
- `trivy-results.txt`: vulnerabilidades encontradas na imagem Docker.

---

## ✅ Próximos passos sugeridos

- Adicionar testes unitários no Gradle (`./gradlew test`)
- Publicar a imagem no Docker Hub (opcional)
- Ativar falha do pipeline com `--failOnCVSS` e `exit-code: 1` para hardening real
- Publicar os relatórios em GitHub Pages ou comentar automaticamente em Pull Requests