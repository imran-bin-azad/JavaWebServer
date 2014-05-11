package net.therap.input;

import net.therap.input.UserInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: imran.azad
 * Date: 5/5/14
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleInput implements UserInput {

    BufferedReader bufferedReader;

    public ConsoleInput() {
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String getString(String name) {
        String line = null;

        try {
            System.out.print(name+": ");
            line = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            return line;
        }

    }

    @Override
    public int getInt(String name) {
        int integer = -1;

        try {
            System.out.print(name+": ");
            integer = Integer.parseInt(bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            return integer;
        }
    }
}
