package net.ofk.kutils

import java.io.IOException

/**
 * Utility to automatically release acquired auto-closable resources.
 * Inside Auto.close {...} block every auto-closable resource
 * should be registered by calling the .open() method on this resources.
 * Such registered resources will be automatically released
 * before leaving the Auto.close block.
 * This also applies even if an unhandled exception passes through the
 * Auto.close block.
 */
class Auto private constructor() {
  companion object {
    /**
     * Any registered auto-closable object
     * will be automatically closed right before leaving the block
     * either normally or by an exception.
     */
    fun <R> close(register: Auto.() -> R): R {
      val manager = Auto()
      return try {
        manager.register()
      } finally {
        manager.close()
      }
    }
  }

  private val resources = arrayListOf<AutoCloseable>()

  /**
   * Registers an auto-closable resource of type T
   * within the current Auto.close block.
   */
  fun <T : AutoCloseable> T?.open(): T? {
    if (this != null) {
      resources.add(this)
    }
    return this
  }

  private fun close() {
    var exception: IOException? = null

    for (it in resources.reversed()) {
      try {
        it.close()
      } catch (ex: Exception) {
        exception = JRE8Utils.INSTANCE.addSuppressed(exception ?: IOException(), ex)
      }
    }
    if (exception != null) {
      throw exception
    }
  }
}

