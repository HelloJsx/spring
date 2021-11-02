package beans.factory.support;

import beans.BeansException;
import beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * 首先在AbstractAutowireCapableBeanFactory类中定义了一个创建对象实例化策略熟悉类InstantiationStrategy，这里我们选择CGLib
 * 接下来注意createBeanInstance这个方法，在这个方法中需要注意Constructor代表有多少个构造函数，通过getDeclaredConstructors()
 * 可以获得所有的构造函数，是一个集合
 * 接下来就需要循环对比出构造函数集合与入参信息args的匹配情况，这里我们对比的方式比较简单，只是一个数量对比，而实际上Spring源码中
 * 还需要对比入参类型，否则相同数量不同入参类型的情况就会抛出异常
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    private InstantiationStrategy instantiationStrategy = (InstantiationStrategy) new CglibSubClassingInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean = null;
        try{
            bean = createBeanInstance(beanDefinition,beanName,args);
        } catch (Exception e) {
            throw  new BeansException("Instantiation of bean failed",e);
        }
        addSingleton(beanName,bean);
        return bean;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition,String beanName,Object[] args){
        Constructor constructorToUse = null;
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        for (Constructor ctor : declaredConstructors){
            if (null != args && ctor.getParameterTypes().length == args.length){
                constructorToUse = ctor;
                break;
            }
        }
        return getInstantiationStrategy().instantiate(beanDefinition,beanName,constructorToUse,args);
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}
