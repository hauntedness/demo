package test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Proxy {

    public static void main(String[] args) {
        // 构建代理对象 : proxy(Target obj)
        // 指明切入点
        // 使用代理
        Proxy proxy1 = new Proxy();
        TargetInterface target = new Target();
        TargetInterface proxy = (TargetInterface) java.lang.reflect.Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), proxy1.getInvocationHandler(target));
        proxy.doSomeThing();
    }

    private InvocationHandler getInvocationHandler(TargetInterface target) {
        return new InvocationHandlerImpl(target);
    }

    class InvocationHandlerImpl implements InvocationHandler {
        private TargetInterface target1;

        public InvocationHandlerImpl(TargetInterface target) {
            target1 = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println(method);
            System.out.println("----------------------------------");
            for (Method e : this.target1.getClass().getMethods()) {
                System.out.println(e);
            }
            System.out.println("----------------------------------");
            System.out.println("InvocationHandlerImp::before");
            method.invoke(target1, args);
            System.out.println("InvocationHandlerImp::after");
            return null;
        }
    }
}
