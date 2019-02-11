package com.boot.hello;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestGreeter {

    @Test
    public void testHello() {
        Greeter greeter = new Greeter();
        Assert.assertEquals("Hello world!", greeter.sayHello()); // g.sayHello();
    }
}
