package beans.factory.support;

import beans.BeansException;
import beans.factory.BeansFactory;
import beans.factory.config.BeanDefinition;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeansFactory {

    @Override
    public Object getBean(String name) throws BeansException{
        Object bean = getSingleton(name);
        if (bean != null){
            return bean;
        }
        BeanDefinition beanDefinition = getBeanDefinition(name);
        return createBean(name,beanDefinition);
    }

    protected abstract Object createBean(String name, BeanDefinition beanDefinition) throws BeansException;

    protected abstract BeanDefinition getBeanDefinition(String name);
}
