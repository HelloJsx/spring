package beans.factory.config;

//注册单例表
public interface SingletonBeanRegistry {
    Object getSingleton(String beanName);
}
