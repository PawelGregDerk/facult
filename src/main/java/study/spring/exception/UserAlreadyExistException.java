package study.spring.exception;

public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException() {
        super("uniqueLogin.message");
    }
}
