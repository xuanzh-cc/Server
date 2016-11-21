package desing.pattern;

/**
 * 懒汉式
 */
public class SingletonLazy {

    //静态实例
    private static SingletonLazy instance = null;

    /**
     * 构造方法私有化
     */
    private SingletonLazy(){}

    /**
     * 单例获取方法
     * @return
     */
    public static synchronized SingletonLazy getInstance(){
        if(instance == null) {
            instance = new SingletonLazy();
        }
        return instance;
    }
}
