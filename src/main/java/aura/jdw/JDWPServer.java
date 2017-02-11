package aura.jdw;

import aura.jdw.protocol.JDWPCommandPacket;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ash on 18/12/2016.
 */
public class JDWPServer {

    private static final String JDWP_HELLO = "JDWP-Handshake";
    private static int HEADER_LENGTH = 11;


    private ServerSocket createServerSocket(int port) {
        try {
            return new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(ServerSocket serverSocket) {
        Socket clientSocket;
        InputStream inStream;
        OutputStream outputStream;

        try {
            clientSocket = serverSocket.accept();
            inStream = clientSocket.getInputStream();
            outputStream = new BufferedOutputStream(clientSocket.getOutputStream());

            waitForHello(inStream, outputStream);

            byte[] inbytes = new byte[HEADER_LENGTH];
            while (inStream.read(inbytes) != -1) {
                JDWPCommandPacket command =  JDWPCommandPacket.createFromHeader(inbytes);

                if (command.getDataSize() > 0) {
                    try {
                        command.setData(getDataFromStream(inStream, command.getDataSize()));
                    } catch (Exception e) {
                        System.err.println("Malformed header - attempted to read data, but data size incorrect");
                    }
                }
                JDWPObservable.create(command).subscribe( replyPacket ->
                        writeAndFlush(outputStream, replyPacket.getBytes()),
                        throwable -> System.err.println("Error processing command - "
                                                        + throwable.getMessage()),
                        () -> closeStream(outputStream));
            }
            closeStream(outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForHello(InputStream inStream, OutputStream outputStream) throws IOException {
        byte[] inbytes = new byte[JDWP_HELLO.length()];
        if (inStream.read(inbytes) != -1) {
            System.out.println("Hello found " + new String(inbytes));
            writeAndFlush(outputStream, JDWP_HELLO.getBytes());
        } else {
            System.err.println("Error initiating JDWP connection");
            closeStream(outputStream);
        }
    }

    private void closeStream(OutputStream outputStream) {
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeAndFlush(OutputStream outputStream, byte[] bytes) {
        try {
            outputStream.write(bytes);
            outputStream.flush();
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
