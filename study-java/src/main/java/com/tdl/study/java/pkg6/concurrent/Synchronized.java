package com.tdl.study.java.pkg6.concurrent;

/**
 * 关键字synchronized可以修饰方法或者以同步块的形式来进行使用，它主要确保多个线程
 * 在同一个时刻，只能有一个线程处于方法或者同步块中，它保证了线程对变量访问的可见性
 * 和排他性
 *
 * 使用 javap –v Synchronized.class 来分析细节
 */
public class Synchronized {
    public static void main(String[] args) {
        /**对对象加锁*/
        synchronized (Synchronized.class) {}
        test();
    }

    public static synchronized void test() {

    }
}

/**
* E:\02_OtherProjects\study-parent\study-java\target\classes\com\tdl\study\java\pkg6\concurrent>javap -v Synchronized.class
* Classfile /E:/02_OtherProjects/study-parent/study-java/target/classes/com/tdl/study/java/pkg6/concurrent/Synchronized.class
* Last modified 2017-6-30; size 621 bytes
*         MD5 checksum 1b0d0ff02024ef96c9bc7a726dffe46a
*         Compiled from "Synchronized.java"
* public class com.tdl.study.java.pkg6.concurrent.Synchronized
*         minor version: 0
*         major version: 52
*         flags: ACC_PUBLIC, ACC_SUPER
*         Constant pool:
*         #1 = Methodref          #4.#23         // java/lang/Object."<init>":()V
*         #2 = Class              #24            // com/tdl/study/java/pkg6/concurrent/Synchronized
*         #3 = Methodref          #2.#25         // com/tdl/study/java/pkg6/concurrent/Synchronized.test:()V
*         #4 = Class              #26            // java/lang/Object
*         #5 = Utf8               <init>
*    #6 = Utf8               ()V
*            #7 = Utf8               Code
*            #8 = Utf8               LineNumberTable
*            #9 = Utf8               LocalVariableTable
*            #10 = Utf8               this
*            #11 = Utf8               Lcom/tdl/study/java/pkg6/concurrent/Synchronized;
*            #12 = Utf8               main
*            #13 = Utf8               ([Ljava/lang/String;)V
*            #14 = Utf8               args
*            #15 = Utf8               [Ljava/lang/String;
*            #16 = Utf8               StackMapTable
*            #17 = Class              #15            // "[Ljava/lang/String;"
*            #18 = Class              #26            // java/lang/Object
*            #19 = Class              #27            // java/lang/Throwable
*            #20 = Utf8               test
*            #21 = Utf8               SourceFile
*            #22 = Utf8               Synchronized.java
*            #23 = NameAndType        #5:#6          // "<init>":()V
*            #24 = Utf8               com/tdl/study/java/pkg6/concurrent/Synchronized
*            #25 = NameAndType        #20:#6         // test:()V
*            #26 = Utf8               java/lang/Object
*            #27 = Utf8               java/lang/Throwable
*            {
* public com.tdl.study.java.pkg6.concurrent.Synchronized();
*         descriptor: ()V
*         flags: ACC_PUBLIC
*         Code:
*         stack=1, locals=1, args_size=1
*         0: aload_0
*         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
*         4: return
*         LineNumberTable:
*         line 10: 0
*         LocalVariableTable:
*         Start  Length  Slot  Name   Signature
*         0       5     0  this   Lcom/tdl/study/java/pkg6/concurrent/Synchronized;
*
* public static void main(java.lang.String[]);
*         descriptor: ([Ljava/lang/String;)V
*         flags: ACC_PUBLIC, ACC_STATIC
*         Code:
*         stack=2, locals=3, args_size=1
*         0: ldc           #2                  // class com/tdl/study/java/pkg6/concurrent/Synchronized
*         2: dup
*         3: astore_1
*         4: monitorenter   notes 监视器进入，获取锁
*         5: aload_1
*         6: monitorexit    notes 监视器退出，释放锁
*         7: goto          15
*         10: astore_2
*         11: aload_1
*         12: monitorexit
*         13: aload_2
*         14: athrow
*         15: invokestatic  #3                  // Method test:()V
*         18: return
*         Exception table:
*         from    to  target type
*         5     7    10   any
*         10    13    10   any
*         LineNumberTable:
*         line 13: 0
*         line 14: 15
*         line 15: 18
*         LocalVariableTable:
*         Start  Length  Slot  Name   Signature
*         0      19     0  args   [Ljava/lang/String;
*         StackMapTable: number_of_entries = 2
*         frame_type = 255 /* full_frame *\/
*         offset_delta = 10
*         locals = [ class "[Ljava/lang/String;", class java/lang/Object ]
*         stack = [ class java/lang/Throwable ]
*         frame_type = 250 /* chop *\/
*         offset_delta = 4
*
* public static synchronized void test();
*         descriptor: ()V
*         flags: ACC_PUBLIC, ACC_STATIC, ACC_SYNCHRONIZED notes 同步方法依靠 ACC_SYNCHRONIZED 修饰符
*         Code:
*         stack=0, locals=0, args_size=0
*         0: return
*         LineNumberTable:
*         line 19: 0
*         }
*         SourceFile: "Synchronized.java"
*/
/**
* 上面class信息中，对于同步块的实现使用了monitorenter和monitorexit指令，而同步方法则
* 是依靠方法修饰符上的ACC_SYNCHRONIZED来完成的。无论采用哪种方式，其本质是对一
* 个对象的监视器（monitor）进行获取，而这个获取过程是排他的，也就是同一时刻只能有一个
* 线程获取到由synchronized所保护对象的监视器
* * * *
* 任意一个对象都拥有自己的监视器，当这个对象由同步块或者这个对象的同步方法调用
* 时，执行方法的线程必须先获取到该对象的监视器才能进入同步块或者同步方法，而没有获
* 取到监视器（执行该方法）的线程将会被阻塞在同步块和同步方法的入口处，进入BLOCKED
* 状态
*/