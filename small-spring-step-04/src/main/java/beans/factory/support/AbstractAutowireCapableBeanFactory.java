package beans.factory.support;

import beans.BeansException;
import beans.PropertyValue;
import beans.PropertyValues;
import beans.factory.config.BeanDefinition;
import beans.factory.config.BeanReference;
import cn.hutool.core.bean.BeanUtil;

import java.lang.reflect.Constructor;

/**
 * 在applyPropertyValues中，通过beanDefinition.getPropertyValues()循环进行属性填充操作，如果遇到的是BeanReference，那么
 * 就需要递归获取Bean实力，调用getBean方法
 * 当把依赖的Bean对象创建完成之后，会递归回到现在属性填充中，这里需要注意没有去处理循环依赖问题
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean = null;
        try {
            bean = createBeanInstance(beanDefinition, beanName, args);
            //给Bean填充属性
            applyPropertyValues(beanName,bean,beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed",e);
        }
        addSingleton(beanName,bean);
        return bean;
    }

    public Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args){
        Constructor constructorToUse = null;
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        for (Constructor ctor: declaredConstructors) {
            if (null != args && ctor.getParameterTypes().length == args.length) {
                constructorToUse = ctor;
                break;
            }
        }
        return getInstantiationStrategy().instantiate(beanDefinition,beanName,constructorToUse,args);
    }

    /**
     * bean属性填充
     * @return
     */
    protected void applyPropertyValues(String beanName,Object bean,BeanDefinition beanDefinition){
        try{
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();

                if (value instanceof BeanReference){
                    //A依赖B，获取B的实例化
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }
                //属性填充
                BeanUtil.setFieldValue(bean,name,value);
            }
        } catch (Exception e){
            throw new BeansException("Error setting property values: " + beanName);
        }
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy){
        this.instantiationStrategy = instantiationStrategy;
    }
}
