# Projeto Android: Gerenciador de Regiões

Este projeto Android permite que os usuários adicionem e visualizem regiões em um mapa, garantindo que novas regiões não estejam muito próximas de regiões existentes.

## Funcionalidades

- Visualização da localização atual do dispositivo em um mapa.
- Adição de novas regiões no mapa.
- Verificação automática se uma nova região está dentro de uma distância segura das regiões existentes.
- Gravação das regiões no banco de dados Firebase.

## Pré-requisitos

- Android Studio instalado.
- Uma conta do Firebase para armazenar os dados das regiões.

## Configuração

1. Clone o repositório do projeto.
2. Abra o projeto no Android Studio.
3. Configure sua conta do Firebase e adicione o arquivo de configuração google-services.json ao diretório app/.
4. Compile e execute o aplicativo em um emulador ou dispositivo Android.

## Estrutura do Projeto

- MainActivity: Classe principal que controla a interface do usuário e a lógica do aplicativo.
- LocationManagerService: Serviço para obter a localização atual do dispositivo.
- RegionManager: Gerencia a adição e verificação de regiões.
- MapManager: Gerencia a exibição do mapa e dos marcadores.
- FirebaseManager: Gerencia a comunicação com o banco de dados Firebase.

## Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para abrir um problema ou enviar um pull request para melhorias no projeto.

## Licença

Este projeto é licenciado sob a [Licença MIT](LICENSE).
