import java.io.*;
import java.net.*;

public class Server {

    private static String[] getListOfFilesInServer(String filesLocation) {
        String[] pathNames;

        // Cria uma nova instancia de File
        File f = new File(filesLocation);

        // Popula pathNames com a lista de nomes arquivos dentro do diretorio
        pathNames = f.list();

        // Retorna a lista de nomes de arquivos
        return pathNames;
    }

    public static void main(String argv[]) throws Exception {
        String clientInput;
        String filesLocation;
        String[] availableFiles;

        ServerSocket serverSocket = new ServerSocket(3333);
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        // Criação do socket e Streams de output e input
        System.out.println("Aguardando conexão de um cliente...");
        Socket connection = serverSocket.accept();
        OutputStream outputStream = connection.getOutputStream();
        BufferedReader fromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        DataOutputStream forClient = new DataOutputStream(outputStream);

        // Solicita localizaçãp dos arquivos do servidor
        // EX: /Users/emilioheinzmann/Documents/git/emilioheinz/socket-files-transmission/src/server_files/
        System.out.println("Onde estão localizados os arquivos do servidor?");
        filesLocation = console.readLine();

        // Busca lista de arquivos disponiveis
        availableFiles = Server.getListOfFilesInServer(filesLocation);

        // Faz a monstagem da string que será retornada para o cliente
        String availableFilesString = "Arquivos disponiveis: ";
        for (int i = 0; i < availableFiles.length; i++) {

            if(i == availableFiles.length - 1) {
                availableFilesString = availableFilesString.concat(availableFiles[i] + ".\n");
            } else {
                availableFilesString = availableFilesString.concat(availableFiles[i] + ", ");
            }
        }

        // Envia os arquivos disponíveis para o cliente
        forClient.writeBytes(availableFilesString);

        // Aguarda resposta do cliente
        System.out.println("Arquivos disponíveis enviados, aguardando escolha...");
        clientInput = fromClient.readLine();

        // Cria um arquivo a partir da localização do arquivo + nome
        File chosenFile = new File(filesLocation + clientInput);
        byte [] fileBytes  = new byte [(int)chosenFile.length()];

        // Cria as streams e le os bites do arquivo para dentro do array de bytes
        FileInputStream fileInputStream = new FileInputStream(chosenFile);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        bufferedInputStream.read(fileBytes,0,fileBytes.length);

        // Envia os bytes para o cliente
        outputStream.write(fileBytes,0,fileBytes.length);
        outputStream.flush();

        System.out.println("Arquivos transferidos, fechando conexão...");
        connection.close();
    }
}

