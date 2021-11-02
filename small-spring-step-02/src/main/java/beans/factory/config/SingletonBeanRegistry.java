package beans.factory.config;

public interface SingletonBeanRegistry {
    //获取单例对象
    Object getSingleton(String beanName);
}
