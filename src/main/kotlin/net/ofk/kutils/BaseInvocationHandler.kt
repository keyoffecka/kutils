package net.ofk.kutils

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

abstract class BaseInvocationHandler : InvocationHandler {
  private val fake = Object()

  override fun invoke(proxy: Any, method: Method, args: Array<Any?>?): Any? =
    when (method.name) {
      "hashCode" -> fake.hashCode() xor 32
      "toString" -> proxy.javaClass.name
      "equals" -> if (args != null && args.size == 1) {
        proxy === args[0]
      } else {
        throw UnsupportedOperationException(method.toString())
      }
      else -> throw UnsupportedOperationException(method.toString())
    }
}