import bean.UserService;
import beans.factory.config.BeanDefinition;
import beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ApiTest {

    @Test
    public void test_BeanFactory(){
        //1.初始化BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //2.注入bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService",beanDefinition);
        //3.获取bean
        UserService userService = (UserService) beanFactory.getBean("userService","dck");
        userService.queryUserInfo();
    }

    @Test
    public void test_cglib(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserService.class);
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
        Object obj = enhancer.create(new Class[]{String.class},new Object[]{"dck"});
        System.out.println(obj);
    }

    @Test
    public void test_newInstance() throws InstantiationException, IllegalAccessException {
        UserService userService = UserService.class.newInstance();
        System.out.println(userService);
    }

    @Test
    public void test_constructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<UserService> userServiceClass = UserService.class;
        Constructor<UserService> declaredConstructors = userServiceClass.getDeclaredConstructor(String.class);
        UserService userService = declaredConstructors.newInstance("dck");
        System.out.println(userService);
    }

    @Test
    public void test_parameterTypes() throws Exception{
        Class<UserService> beanClass = UserService.class;
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        Constructor<?> constructor = declaredConstructors[0];
        Constructor<UserService> declaredConstructor = beanClass.getDeclaredConstructor(constructor.getParameterTypes());
        UserService userService = declaredConstructor.newInstance("dck");
        System.out.println(userService);
    }
}
