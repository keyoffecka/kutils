package net.ofk.kutils

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * Base implementation for proxy handlers.
 */
abstract class BaseInvocationHandler : InvocationHandler {
  open fun doInvoke(proxy: Any, method: Method, args: Array<Any?>?): Any? =
    throw UnsupportedOperationException(method.toGenericString())

  override fun invoke(proxy: Any, method: Method, args: Array<Any?>?): Any? =
    if (method.name == "hashCode") {
      hashCode() xor 32
    } else if (method.name == "toString") {
      proxy.javaClass.name
    } else if (method.name == "equals" && args != null && args.size == 1 && method.parameterTypes[0] == Object::class.java) {
      proxy === args[0]
    } else {
      doInvoke(proxy, method, args)
    }
}