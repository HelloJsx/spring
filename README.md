# spring
spring核心代码实现
## 02

> 目标

​	这一次我们把 Bean 的创建交给容器，而不是我们在调用时候传递一个实例化 好的 Bean 对象，另外还需要考虑单例对象，在对象的二次获取时是可以从内 存中获取对象的。此外不仅要实现功能还需要完善基础容器框架的类结构体， 否则将来就很难扩容进去其他的功能了。

> 设计

​	鉴于本节的案例目标，我们需要将 Spring Bean 容器完善起来，首先非常重 要的一点是在 Bean 注册的时候只注册一个类信息，而不会直接把实例化信息 注册到 Spring 容器中。那么就需要修改 BeanDefinition 中的属性 Object  为 Class，接下来在需要做的就是在获取 Bean 对象时需要处理 Bean 对象的 实例化操作以及判断当前单例对象在容器中是否已经缓存起来了。

- 首先我们需要定义 BeanFactory 这样一个 Bean 工厂，提供 Bean 的获取方法 getBean(String name)，之后这个 Bean 工厂接口由抽象类 AbstractBeanFactory 实现。这样使用模板模式的设计方式，可以统一收口通用核 心方法的调用逻辑和标准定义，也就很好的控制了后续的实现者不用关心调用逻 辑，按照统一方式执行。那么类的继承者只需要关心具体方法的逻辑实现即可。
- 那么在继承抽象类 AbstractBeanFactory 后的 AbstractAutowireCapableBeanFactory 就可以实现相应的抽象方法了，因为 AbstractAutowireCapableBeanFactory 本身也是一个抽象类，所以它只会实现属于 自己的抽象方法，其他抽象方法由继承 AbstractAutowireCapableBeanFactory 的 类实现。这里就体现了类实现过程中的各司其职，你只需要关心属于你的内容，不 是你的内容，不要参与。这一部分内容我们会在代码里有具体的体现 
- 另外这里还有块非常重要的知识点，就是关于单例 SingletonBeanRegistry 的接口 定义实现，而 DefaultSingletonBeanRegistry 对接口实现后，会被抽象类 AbstractBeanFactory 继承。现在 AbstractBeanFactory 就是一个非常完整且强大的 抽象类了，也能非常好的体现出它对模板模式的抽象定义。接下来我们就带着这些 设计层面的思考，去看代码的具体实现结果

> 总结

- 本章节中加强了功能的完 善。在实现的过程中也可以看到类的关系变得越来越多了，如果没有做过一些稍微 复杂的系统类系统，那么即使现在这样 9 个类搭出来的容器工厂也可以给你绕晕。
- 在 Spring Bean 容器的实现类中要重点关注类之间的职责和关系，几乎所有的程 序功能设计都离不开接口、抽象类、实现、继承，而这些不同特性类的使用就可以 非常好的隔离开类的功能职责和作用范围。而这样的知识点也是在学习手写 Spring Bean 容器框架过程非常重要的知识。

## 03

> 目标

​	解决一个Bean对象在含有构造函数进行实例化的坑，在上一节中我们将实例化对象交给容器来统一处理，但在我们实例化对象的代码里并没有考虑对象类是否含有构造函数，也就是说我们去实例化一个含有构造函数的Bean就会抛出异常

​	解决办法：在UserService中添加一个含入参信息的构造函数就行

> 设计

​	这个设计分为两个部分，一个部分是串流程从哪合理的把构造函数的入参信息传递到实例化操作里，另外一个是怎么去实例化含有构造函数的对象

​	参考Spring Bean容器源码的实现方式，在BeanFactory中添加Object getBean(String name,Object... args)接口，这样就可以在获取Bean的时候把构造函数的入参信息传入进去了

​	另外一个核心的内容是使用Java本身自带的方法DeclaredConstructor，或者是使用CGlib来动态的创建Bean对象来创建含有构造函数的Bean对象

> 总结

1.在BeanFactory接口中新增一个getBean接口，BeanFactory中我们重载一个含有入参信息args的getBean方法，这样就可以方便的传递入参给构造函数实例化了

2.定义实例化策略接口InstantiationStrategy，在实例化接口instantiate方法中添加必要的入参信息，包括beanDefinition，beanName，constructor，args。其中Constructor里面包含了一些必要的类信息，有这个参数的目的就是为了拿到符合入参信息相对应的构造参数，args是一个具体的入参信息了，最终实例化的时候会用到

3.在SimpleInstantiationStrategy类中进行JDK实例化，首先通过beanDefinition获取Class信息，这个Class信息是在Bean定义的时候传递进去的，接下来判断ctor是否为空，如果为空则是无构造函数实例化，否则就是需要有构造函数的实例化，有构造函数的实例化的方式是clazz.getDeclaredConstructor(constructor.getParameterTypes()).newInstance(args)，把入参的信息传递给newInstance进行实例化

4.在AbstractAutowireCapableBeanFactory类中创建策略调用，首先在AbstractAutowireCapableBeanFactory类中定义了一个创建对象实例化策略熟悉类InstantiationStrategy，这里我们选择CGLib，接下来注意createBeanInstance这个方法，在这个方法中需要注意Constructor代表有多少个构造函数，通过getDeclaredConstructors()可以获得所有的构造函数，是一个集合，接下来就需要循环对比出构造函数集合与入参信息args的匹配情况，这里我们对比的方式比较简单，只是一个数量对比，而实际上Spring源码中还需要对比入参类型，否则相同数量不同入参类型的情况就会抛出异常

## 04

> 目标

​	解决类中是否有属性的问题，如果有类中包含属性那么在实例化的时候就要把属性信息天上，这样才是一个完整的对象创建，对于属性填充不只是String那些，还包括还没有实例化的对象属性，都需要在Bean创建的时候进行填充操作。

> 设计

1.属性填充要在类实例化创建之后，也就是需要在AbstractAutowireCapableBeanFactory 的 createBean 方法中添加 applyPropertyValues 操作。

2.由于需要在创建Bean的时候填充属性操作，那么就需要再定义一个BeanDefinition类中添加PropertyValue信息

3.填充属性信息还包括了Bean的对象类型，也就是需要再定义一个BeanReference，里面其实就是一个简单的Bean名称，再具体的实例化操作的时候进行递归创建和填充

> 总结

1.Bean注册的时候需要传递的信息为了把属性一定交给Bean定义，所以这里填充了PropertyValues属性，同时把两个构造函数做了一些简单的优化，避免后面for循环时还得判断属性填充是否为空

2.在applyPropertyValues中，通过beanDefinition.getPropertyValues()循环进行属性填充操作，如果遇到的是BeanReference，那么就需要递归获取Bean实力，调用getBean方法当把依赖的Bean对象创建完成之后，会递归回到现在属性填充中，这里需要注意没有去处理循环依赖问题
