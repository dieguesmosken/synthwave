<div align="center">
<img width="1200" height="475" alt="GHBanner" src="https://ai.google.dev/static/site-assets/images/share-ais-513315318.png" />
</div>

# SoundWave - Synthwave Music Player

Bem-vindo ao **SoundWave**, um aplicativo reprodutor de música inspirado pela estética retrowave, synthwave e cyberpunk, desenvolvido com Jetpack Compose!

## Principais Funcionalidades Adicionadas
- **Login com Google:** Autenticação fácil e segura utilizando o Android Credential Manager.
- **Integração com MongoDB:** Salva e gerencia os perfis dos usuários conectados.
- **Integração com Spotify:** Traz resultados reais de pesquisa de músicas e álbuns através de uma API em um ViewModel.

## Como Executar Localmente

**Pré-requisitos:** [Android Studio](https://developer.android.com/studio) atualizado e emulador ou dispositivo físico rodando Android 7.0 (API 24) ou superior.

1. Abra o Android Studio.
2. Selecione **Open** e escolha o diretório contendo este projeto.
3. Aguarde o Gradle sincronizar todas as novas dependências (MongoDB, Credential Manager, Retrofit).
4. Crie um arquivo chamado `.env` no diretório do projeto, contendo as suas chaves necessárias.
5. Remova esta linha do arquivo `app/build.gradle.kts`: `signingConfig = signingConfigs.getByName("debugConfig")` caso tenha problemas de assinatura no debug.
6. Execute o app no emulador ou dispositivo clicando em **Run** (`Shift + F10`).

### Notas Adicionais para Funcionalidades Específicas
- **Spotify API:** Para utilizar as buscas reais, você precisará de um `AccessToken` válido. O App hoje tem um espaço reservado em `MusicViewModel.kt` com `MOCK_SPOTIFY_TOKEN`. Para obter um token real, registre uma aplicação no [Spotify Developer Dashboard](https://developer.spotify.com/dashboard).
- **Google Login:** Para que o Google Sign-In funcione corretamente em produção, certifique-se de configurar a sua impressão digital (SHA-1) e criar o `Web Client ID` no Google Cloud Console, inserindo o ID no arquivo `GoogleAuthClient.kt`.
