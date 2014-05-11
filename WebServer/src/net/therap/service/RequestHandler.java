package net.therap.service;

import net.therap.http.HttpRequest;
import net.therap.http.HttpResponse;
import net.therap.http.StatusMessage;

import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: imran.azad
 * Date: 5/6/14
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class RequestHandler implements Runnable {

    private Socket clientSocket;
    private String homeDirectory;

    public RequestHandler(Socket clientSocket, String homeDirectory) {
        this.clientSocket = clientSocket;
        this.homeDirectory = homeDirectory;
    }

    @Override
    public void run() {
        System.out.println("NEW THREAD");
        talkToClient();
    }

    private void talkToClient() {
        try(
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            HttpRequest httpRequest = new HttpRequest();
            HttpResponse httpResponse = new HttpResponse(outToClient);

            httpRequest.receiveRequest(inFromClient);

            System.out.println("\nTime to RESPOND!!\n");


            if (httpRequest.requestMethod.equals("GET")) {
                handleGetRequests(httpRequest, httpResponse);
            }

            else if (httpRequest.requestMethod.equals("POST")) {
                handlePostRequests(httpRequest, httpResponse);
            }

            else {
                httpResponse.sendErrorMessage(StatusMessage.BadRequest, httpRequest.protocolVersion);
            }

            System.out.println("\nResponse sent\n");

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void handleGetRequests(HttpRequest httpRequest, HttpResponse httpResponse) {
        File requestedFile = new File(homeDirectory + httpRequest.requestURL);

        if (!requestedFile.exists()) {
            httpResponse.sendErrorMessage(StatusMessage.NotFound, httpRequest.protocolVersion);
        }

        else {
            httpResponse.sendOKMessage(httpRequest.protocolVersion);

            if (httpRequest.requestURL.equals("/")) {
                // send login.html by default
                httpResponse.sendResponse(new File(homeDirectory + "/login.html"));
            }

            else {
                if(requestedFile.exists() && !requestedFile.isDirectory()) {
                    httpResponse.sendResponse(requestedFile);
                }
            }
        }
    }

    private void handlePostRequests(HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.sendOKMessage(httpRequest.protocolVersion);
        httpResponse.sendResponse(new File(homeDirectory + httpRequest.requestURL));
    }

}
