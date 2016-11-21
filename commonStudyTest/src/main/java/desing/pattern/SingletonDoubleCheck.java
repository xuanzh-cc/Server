package desing.pattern;

/**
 * 饿汉式
 */
public class SingletonDoubleCheck {

    //静态实例
    private static SingletonDoubleCheck instance = null;

    /**
     * 构造方法私有化
     */
    private SingletonDoubleCheck(){}

    /**
     * 单例获取方法
     * @return
     */
    public static SingletonDoubleCheck getInstance(){

        if (instance == null) {
            //1 这里可能会因为并发问题，而同时执行到这里
            synchronized (SingletonDoubleCheck.class) {
                //如果一个线程先执行到了这里，那么下次另一个线程执行到了位置1后，也会执行到这里，所以这里再加一次判断库避免之前被new 出来的实例被覆盖。
                if (instance == null) {
                    instance = new SingletonDoubleCheck();
                }
                return instance;
            }
        }
        return instance;
    }
}
