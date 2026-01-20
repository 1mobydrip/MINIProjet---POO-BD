package tn.univ.pharmacie.exception;

public class StockInsuffisantException extends RuntimeException{
    public StockInsuffisantException(String msg){
        super(msg);
    }
}
