package net.ofk.kutils

import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import java.io.ByteArrayInputStream
import java.io.IOException

class AutoTest {
  @Test
  fun test() {
    var s: AutoCloseable = Mockito.spy(ByteArrayInputStream("test".toByteArray()))

    Auto.close {
      s.open()
    }

    Mockito.verify(s).close()

    s = Mockito.spy(ByteArrayInputStream("test".toByteArray()))
    var s2: AutoCloseable = Mockito.spy(ByteArrayInputStream("test".toByteArray()))

    Auto.close {
      s.open()
      s2.open()
    }

    var o = Mockito.inOrder(s, s2);
    o.verify(s2).close()
    o.verify(s).close()

    s = Mockito.spy(ByteArrayInputStream("test".toByteArray()))

    val e = Exception()
    try {
      Auto.close {
        s.open()
        throw e
      }
    } catch(ex: Exception) {
      Assert.assertSame(e, ex);
    }

    Mockito.verify(s).close()

    s = Mockito.spy(ByteArrayInputStream("test".toByteArray()))
    s2 = Mockito.spy(ByteArrayInputStream("test".toByteArray()))

    try {
      Auto.close {
        s.open()
        s2.open()
        throw e
      }
    } catch(ex: Exception) {
      Assert.assertSame(e, ex);
    }

    o = Mockito.inOrder(s, s2);
    o.verify(s2).close()
    o.verify(s).close()

    s = Mockito.spy(ByteArrayInputStream("test".toByteArray()))
    s2 = Mockito.mock(AutoCloseable::class.java)
    val s3 = Mockito.mock(AutoCloseable::class.java)
    val e2 = Exception()

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
      Assert.assertArrayEquals(arrayOf<Throwable>(e2, e), JRE8Utils.getSuppressed(ex));
    }

    o = Mockito.inOrder(s, s2, s3)
    o.verify(s3).close()
    o.verify(s2).close()
    o.verify(s).close()
  }
}