package desing.pattern;

/**
 * 静态内部类
 */
public class SingletonInnerClass {

    /**
     * 构造方法私有化
     */
    private SingletonInnerClass(){}

    /**
     * 单例获取方法
     * @return
     */
    public static SingletonInnerClass getInstance(){
        return InnerHolder.instance;
    }

    /**
     * 静态内部类
     */
    private static class InnerHolder {
        private static SingletonInnerClass instance = new SingletonInnerClass();
    }
}
