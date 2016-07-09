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

    try {
      (p as C).equals()
      Assert.fail()
    } catch(e: UnsupportedOperationException) {
      Assert.assertEquals("public abstract boolean net.ofk.kutils.C.equals()", e.message)
    }

    try {
      (p as C).equals(1)
      Assert.fail()
    } catch(e: UnsupportedOperationException) {
      Assert.assertEquals("public abstract java.lang.String net.ofk.kutils.C.equals(int)", e.message)
    }

    try {
      (p as C).equals(1, 2)
      Assert.fail()
    } catch(e: UnsupportedOperationException) {
      Assert.assertEquals("public abstract java.lang.String net.ofk.kutils.C.equals(int,long)", e.message)
    }

    try {
      (p as C).m()
      Assert.fail()
    } catch(e: UnsupportedOperationException) {
      Assert.assertEquals("public abstract java.lang.String net.ofk.kutils.C.m()", e.message)
    }
  }
}

interface C {
  fun equals(): Boolean;
  fun equals(a: Int): String;
  fun equals(a: Int, b: Long): String;
  fun m(): String;
}