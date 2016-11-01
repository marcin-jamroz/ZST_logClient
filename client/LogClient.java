package client;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * class which is implementing logClient
 *
 * @author Marcin Jamroz
 */


public class LogClient extends Thread {

    private String hostName = "localhost";

    private int portNumber = 5555;

    private Socket clientSocket;


    public LogClient() {
        try {
            System.out.println("Connecting to " + hostName + " on port " + portNumber);
            clientSocket = new Socket(hostName, portNumber);
            System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String data) {

        try {
            OutputStream outData = clientSocket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outData);
            out.writeUTF(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String readMessage() {
        DataInputStream inData;
        String data = "";
        try {
            inData = new DataInputStream(clientSocket.getInputStream());
            data = inData.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Sudden socket close");
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return data;
    }

    public static void main(String[] args) {

        LogClient logClient = new LogClient();

        boolean dataFlag = true;

        int i = 0; //for testing purposes

        while (dataFlag && i<=101) {

            i++;

           // BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
           // System.out.print("Enter log text in given order: logname, data or type 'exit' to exit logClient");

            String message = "";

            message = "simpleLog Kappa";
            if (i==101) message = "exit";

           /* try {
                System.out.println();
              message = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            if (Objects.equals("exit", message)) {
                dataFlag = false;
                logClient.sendData(message);
                String answer = logClient.readMessage();
                while(!Objects.equals(answer, "ok")) System.out.println("cap");
                System.out.println("LogClient turned off");
            } else {
                logClient.sendData(message);
                System.out.println();
                System.out.println("Data '" + message + "' sent");
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

          /*try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

    }




}

