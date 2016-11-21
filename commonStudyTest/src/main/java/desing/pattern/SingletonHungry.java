package desing.pattern;

/**
 * 饿汉式
 */
public class SingletonHungry {

    //静态实例
    private static SingletonHungry instance = new SingletonHungry();

    /**
     * 构造方法私有化
     */
    private SingletonHungry(){}

    /**
     * 单例获取方法
     * @return
     */
    public static SingletonHungry getInstance(){
        return instance;
    }
}
