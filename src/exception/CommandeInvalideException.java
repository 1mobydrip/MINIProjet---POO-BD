package exception;

public class CommandeInvalideException extends RuntimeException{
    public CommandeInvalideException(String msg){
        super(msg);
    }
}
