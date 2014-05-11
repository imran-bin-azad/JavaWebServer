package net.therap.input;

import net.therap.input.UserInput;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: imran.azad
 * Date: 5/11/14
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesInput implements UserInput {

    private ResourceBundle serverInfo;

    public PropertiesInput(String fileName) {
        try {
            serverInfo = new PropertyResourceBundle(new FileInputStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    @Override
    public String getString(String name) {
        return serverInfo.getString(name);
    }

    @Override
    public int getInt(String name) {
        return Integer.parseInt(serverInfo.getString(name));
    }
}

