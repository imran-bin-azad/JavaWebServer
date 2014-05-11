package net.therap;

import input.PropertiesInput;
import input.UserInput;
import service.HttpServer;

public class Main {

    public static void main(String args[]) {
        UserInput userInput = new PropertiesInput("server.properties");
        int portNumber = userInput.getInt("port.number");
        String rootDirectory = userInput.getString("root.directory");

        HttpServer httpServer = new HttpServer(rootDirectory);
        httpServer.runServer(portNumber);
    }

}
