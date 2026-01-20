package tn.univ.pharmacie.exception;

public class CommandeInvalideException extends RuntimeException{
    public CommandeInvalideException(String msg){
        super(msg);
    }
}
