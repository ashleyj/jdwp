package aura.jdw;

import aura.jdw.JDWPObservable;
import aura.jdw.protocol.JDWPCommandPacket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by ash on 18/12/2016.
 */
public class JDWPServer {

    private static final long INITIAL_DELAY = 10;
    private static final String JDWP_HELLO = "JDWP-Handshake";
    private static int port = 8000;
    private static int HEADER_LENGTH = 11;

    public static void main (String[] args) {
        ServerSocket serverSocket;
        Socket clientSocket;
        InputStream inStream;
        OutputStream outputStream;

        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            inStream = clientSocket.getInputStream();
            outputStream = new BufferedOutputStream(clientSocket.getOutputStream());

            byte[] inbytes = new byte[JDWP_HELLO.length()];
            if (inStream.read(inbytes) != -1) {
                System.out.println("Hello found " + new String(inbytes));
                outputStream.write(JDWP_HELLO.getBytes());
                outputStream.flush();
            }

            inbytes = new byte[HEADER_LENGTH];
            while (inStream.read(inbytes) != -1) {
                JDWPCommandPacket command =  JDWPCommandPacket.createFromHeader(inbytes);

                if (command.getDataSize() > 0) {
                    try {
                        command.setData(getDataFromStream(inStream, command.getDataSize()));
                    } catch (Exception e) {
                        System.err.println("Malformed header - attempted to read data, but data size incorrect");
                    }
                }
                JDWPObservable.create(command)
                        .doOnNext(replyPacket -> {
                            try {
                                outputStream.write(replyPacket.getBytes());
                                outputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        })
                        .doOnCompleted(() -> {
                                    try {
                                        System.err.println("Closing connection");
                                        outputStream.flush();
                                        outputStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                        )
                        .doOnError(error -> {
                           System.err.println("Error processing command - " + error);
                        }).subscribe();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] getDataFromStream(InputStream inStream, int count) throws IOException {
            byte[] data = new byte[count];
            if (inStream.read(data) != -1) {
                return data;
            }
            return null;
    }

}
