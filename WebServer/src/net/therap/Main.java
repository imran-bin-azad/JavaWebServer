package net.therap;


import net.therap.input.PropertiesInput;
import net.therap.input.UserInput;
import net.therap.service.HttpServer;

public class Main {

    public static void main(String args[]) {
        UserInput userInput = new PropertiesInput("server.properties");
        int portNumber = userInput.getInt("port.number");
        String rootDirectory = userInput.getString("root.directory");

        HttpServer httpServer = new HttpServer(rootDirectory);
        httpServer.runServer(portNumber);
    }

}
