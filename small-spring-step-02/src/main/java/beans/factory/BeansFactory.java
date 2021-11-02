package beans.factory;

public interface BeansFactory {
    Object getBean(String name) throws beans.BeansException;
}
