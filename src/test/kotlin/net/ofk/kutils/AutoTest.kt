package net.ofk.kutils

import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import java.io.IOException

class AutoTest {
  @Test
  fun test() {
    var s = Mockito.mock(AutoCloseable::class.java)
    var s2 = Mockito.mock(AutoCloseable::class.java)
    var s3 = Mockito.mock(AutoCloseable::class.java)
    val e = Exception()
    val e2 = Exception()

    Auto.close {
      s = Mockito.mock(AutoCloseable::class.java).open()
    }

    Mockito.verify(s).close()

    Auto.close {
      s = Mockito.mock(AutoCloseable::class.java).open()
      s2 = Mockito.spy(AutoCloseable::class.java).open()
    }

    var o = Mockito.inOrder(s, s2);
    o.verify(s2).close()
    o.verify(s).close()

    try {
      Auto.close {
        s = Mockito.mock(AutoCloseable::class.java).open()
        throw e
      }
    } catch(ex: Exception) {
      Assert.assertSame(e, ex);
    }

    Mockito.verify(s).close()

    try {
      Auto.close {
        s = Mockito.mock(AutoCloseable::class.java).open()
        s2 = Mockito.mock(AutoCloseable::class.java).open()
        throw e
      }
    } catch(ex: Exception) {
      Assert.assertSame(e, ex);
    }

    o = Mockito.inOrder(s, s2);
    o.verify(s2).close()
    o.verify(s).close()

    s = Mockito.mock(AutoCloseable::class.java)
    s3 = Mockito.mock(AutoCloseable::class.java)

    Mockito.doThrow(e).`when`(s2).close()
    Mockito.doThrow(e2).`when`(s3).close()

    try {
      Auto.close {
        s.open()
        s2.open()
        s3.open()
      }
      Assert.fail()
    } catch(ex: IOException) {
      Assert.assertArrayEquals(arrayOf<Throwable>(e2, e), JRE8Utils.INSTANCE.getSuppressed(ex));
    }

    o = Mockito.inOrder(s, s2, s3)
    o.verify(s3).close()
    o.verify(s2).close()
    o.verify(s).close()

    val ac = Auto.close {
      (null as AutoCloseable?).open()
    }
    Assert.assertNull(ac);
  }
}