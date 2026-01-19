package exception;

public class StockInsuffisantException extends RuntimeException{
    public StockInsuffisantException(String msg){
        super(msg);
    }
}
