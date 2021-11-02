package beans.factory;

import beans.BeansException;

//BeanFactory中我们重载一个含有入参信息args的getBean方法，这样就可以方便的传递入参给构造函数实例化了
public interface BeanFactory {

    Object getBean(String name) throws BeansException;

    Object getBean(String name,Object... args) throws BeansException;
}
