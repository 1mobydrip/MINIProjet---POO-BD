package tn.univ.pharmacie.exception;

public class MedicamentInexistantException extends RuntimeException{
    public MedicamentInexistantException(String msg){
        super(msg);
    }
}
