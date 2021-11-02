import bean.UserDao;
import bean.UserService;
import beans.PropertyValue;
import beans.PropertyValues;
import beans.factory.config.BeanDefinition;
import beans.factory.config.BeanReference;
import beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Test;

public class ApiTest {

    @Test
    public void test_BeanFactory(){
        //1.初始化BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //2.UserDao注册
        beanFactory.registerBeanDefinition("userDao",new BeanDefinition(UserDao.class));
        //3.UserService设置属性[uId、userDao]
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("uId","10001"));
        propertyValues.addPropertyValue(new PropertyValue("userDao",new BeanReference("userDao")));
        //4.UserService注入bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class,propertyValues);
        beanFactory.registerBeanDefinition("userService",beanDefinition);
        //5.UserService获取bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
    }
}
