package net.therap.http;

/**
 * Created with IntelliJ IDEA.
 * User: imran.azad
 * Date: 5/7/14
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public enum StatusMessage {

    OK(200,"OK"), NotFound(404,"Not Found"), BadRequest(400,"Bad Request");

    int number;
    String message;

    StatusMessage(int number, String message) {
        this.number = number;
        this.message = message;
    }

}
