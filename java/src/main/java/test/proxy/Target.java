package test.proxy;

public class Target implements TargetInterface {
    @Override
    public void doSomeThing() {
        System.out.println("Target::doSomeThing");
    }
}
