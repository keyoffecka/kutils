package net.ofk.kutils

import org.junit.Assert
import org.junit.Test
import java.lang.reflect.Proxy

class BaseInvocationHandlerTest {
  @Test
  fun testInvoke() {
    val p = Proxy.newProxyInstance(C::class.java.classLoader, arrayOf(C::class.java), object: BaseInvocationHandler(){}) as Object
    Assert.assertNotNull(p.hashCode());
    Assert.assertFalse(p.equals(null));
    Assert.assertFalse(p.equals(Any()));
    Assert.assertTrue(p.equals(p));
    Assert.assertTrue(p.toString().isNotBlank());
  }
}

interface C