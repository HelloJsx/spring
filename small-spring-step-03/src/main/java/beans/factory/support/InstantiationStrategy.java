package beans.factory.support;

import beans.BeansException;
import beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;


/**
 * 在实例化接口instantiate方法中添加必要的入参信息，包括beanDefinition，beanName，constructor，args
 * 其中Constructor里面包含了一些必要的类信息，有这个参数的目的就是为了拿到符合入参信息相对应的构造参数
 * args是一个具体的入参信息了，最终实例化的时候会用到
 */
public interface InstantiationStrategy {

    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor constructor,Object[] args) throws BeansException;
}
