package me.hizencode.doctormanagement.user.profile;

public class UserProfileAlreadyExistsException extends Throwable{
    public UserProfileAlreadyExistsException(String message) {
        super(message);
    }
}
