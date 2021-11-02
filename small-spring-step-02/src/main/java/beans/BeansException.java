package beans;

public class BeansException extends Throwable {

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg,Throwable e) {
        super(msg,e);
    }
}
