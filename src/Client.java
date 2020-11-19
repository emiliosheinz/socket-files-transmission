import java.io.*;
import java.net.*;

public class Client {

    public static void main(String argv[]) {
        // Declaração inicial de variaveis
        String userInput;
        String serverOutput;
        BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in));
        String clientFilesLocation;

        try{
            // Criação do socket e Streams de output e input
            Socket connection = new Socket("localhost", 3333);
            InputStream inputStream = connection.getInputStream();
            DataOutputStream toServer =new DataOutputStream(connection.getOutputStream());
            BufferedReader fromServer =new BufferedReader(new InputStreamReader(inputStream));

            // Espera o servidor enviar algo e printa na tela (arquivos disponíveis)
            System.out.println("Aguardando resposta do servidor...");
            serverOutput = fromServer.readLine();
            System.out.println(serverOutput);

            // Solicita que seja feita a escolha de um arquivo e envia para o servidor
            System.out.println("Digite o nome de um dos arquivos: ");
            userInput = fromUser.readLine();
            toServer.writeBytes(userInput + "\n");

            // Le os bites de entrada e guarda os em uma variavel
            byte [] myByteArray  = new byte [6022386];
            int bytesRead = inputStream.read(myByteArray,0,myByteArray.length);
            int current = bytesRead;
            do {
                bytesRead = inputStream.read(myByteArray, current, (myByteArray.length-current));

                if (bytesRead >= 0) {
                    current += bytesRead;
                }
            } while(bytesRead > -1);

            // EX: /Users/emilioheinzmann/Documents/git/emilioheinz/socket-files-transmission/src/client_files/
            System.out.println("Onde você gostaria de salvar esse arquivo?");
            clientFilesLocation = fromUser.readLine();

            // Escreve os bytes lidos em um novo arquivo, no servidos e então fecha conexão.
            FileOutputStream fileOutputStream= new FileOutputStream(clientFilesLocation + userInput);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(myByteArray, 0 , current);
            bufferedOutputStream.flush();

            System.out.println("Arquivo salvo, fechando conexão...");
            connection.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}