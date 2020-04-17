package aaa.bbb;

public class Test
{
    static {
        System.out.println("aaa.bbb.Test class Loaded  [VERSION-1.1]");
    }
    public String sayHello(String name)
    {
        System.out.println("aaa.bbb.Test sayHello() called v1.1");
        return "Mr. " +name;
    }
}
