package net.therap.service;

import net.therap.service.RequestHandler;
import net.therap.service.ThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: imran.azad
 * Date: 5/7/14
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class HttpServer {

    private final int THREAD_COUNT = 20;
    private String rootDirectory;

    public HttpServer(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public void runServer(int portNumber) {
        try (
            ServerSocket serverSocket = new ServerSocket(portNumber);
            ThreadPool threadPool = new ThreadPool(THREAD_COUNT);
        ) {
            System.out.println("Server is Running");

            Socket clientSocket;
            while (true) {
                clientSocket = serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(clientSocket, rootDirectory);
                threadPool.runNewThread(requestHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
