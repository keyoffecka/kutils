package net.ofk.utils;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A set of lambda-function wrappers to help implementing code that throws checked exceptions.
 */
public class Executor {
  <I, O, E extends Exception> O convert(final Executor.Converter<I, O> converter, final I param) throws E {
    O result = null;
    try {
      result = converter.convert(param);
    } catch (final RuntimeException ex) {
      throw ex;
    } catch (final Exception ex) {
      throw (E) ex;
    }
    return result;
  }

  /**
   * Wraps executable into runnable.
   * Checked exceptions will be wrapped into unchecked UndeclaredThrowableException.
   */
  public Runnable toRunnable(final Executor.Executable executable) {
    return () -> this.execute(executable);
  }

  /**
   * Wraps converter into function.
   * Checked exceptions will be wrapped into unchecked UndeclaredThrowableException.
   */
  public <I, O> Function<I, O> toFunction(final Executor.Converter<I, O> converter) {
    return param -> this.convert(converter, param);
  }

  /**
   * Wraps executable into runnable.
   * Checked exceptions will be wrapped into unchecked UndeclaredThrowableException.
   */
  public <T> Supplier<T> toSupplier(final Callable<T> provider) {
    return () -> this.provide(provider);
  }

  public <T> Consumer<T> toConsumer(final Executor.Processor<T> processor) {
    return param -> this.convert(p -> {
      processor.process(p);
      return null;
    }, param);
  }

  /**
   * Runs a code that doesn't return a value but is allowed to throw checked exceptions.
   */
  public void execute(final Executor.Executable executable) {
    this.convert(param -> {
      executable.execute();
      return null;
    }, null);
  }

  /**
   * Runs a code that returns a value and is allowed to throw checked exceptions.
   */
  public <T> T provide(final Callable<T> callable) {
    return this.convert(param -> callable.call(), null);
  }

  /**
   * Wrapper for Runnable.
   */
  public interface Executable {
    void execute() throws Exception;
  }

  /**
   * Wrapper for Consumer.
   */
  public interface Processor<T> {
    void process(T param) throws Exception;
  }

  /**
   * Wrapper for Function.
   */
  public interface Converter<I, O> {
    O convert(I param) throws Exception;
  }

  //Callable is a standard wrapper for Supplier
}
