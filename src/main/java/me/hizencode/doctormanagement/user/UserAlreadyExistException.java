package me.hizencode.doctormanagement.user;

public class UserAlreadyExistException extends Throwable {

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
