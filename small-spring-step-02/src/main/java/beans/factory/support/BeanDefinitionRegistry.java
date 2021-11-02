package beans.factory.support;

import beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
    //向注册表中注册BeanDefinition
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
