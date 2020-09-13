package me.hizencode.mededu.user;

public class UserAlreadyExistException extends Throwable {

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
