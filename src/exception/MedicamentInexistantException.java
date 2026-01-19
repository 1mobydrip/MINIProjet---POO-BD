package exception;

public class MedicamentInexistantException extends RuntimeException{
    public MedicamentInexistantException(String msg){
        super(msg);
    }
}
