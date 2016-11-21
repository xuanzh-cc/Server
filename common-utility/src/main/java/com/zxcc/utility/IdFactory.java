package com.zxcc.utility;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ID 生成工具
 * Created by xuanzh.cc on 2016/9/8.
 */
public class IdFactory {

    public static final AtomicInteger gen = new AtomicInteger(10000);

    /**
     * 获得下一个ID  64位
     * @return
     */
    public static long nextId64Bit(){
        long l = DateUtils.currentTimeMillis();
        return (l << 22) + gen.incrementAndGet();
    }

    /**
     * 获得下一个ID  32位，（该方法有bug，暂时不建议使用）
     * @return
     */
    @Deprecated()
    public static long nextId32Bit(){
        return gen.incrementAndGet() << 16 + gen.incrementAndGet();
    }


    public static void main(String[] args) {
        /*System.out.println(Long.toBinaryString(DateUtils.currentTimeMillis()));
        System.out.println(Long.toBinaryString(DateUtils.currentTimeMillis()).length());
        System.out.println(Long.toBinaryString(DateUtils.currentTimeMillis() << 16));
        System.out.println(Long.toBinaryString(DateUtils.currentTimeMillis() << 16).length());
        System.out.println(Long.toBinaryString( DateUtils.currentTimeMillis()).length());*/
        for (int i = 1; i <= 500; i++){
            System.out.println(nextId64Bit());
        }
    }
}
