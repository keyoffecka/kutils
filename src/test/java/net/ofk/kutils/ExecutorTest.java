package net.ofk.kutils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExecutorTest {
  private Executor executor;

  @Before
  public void setUp() {
    this.executor = new Executor();
  }

  @Test
  public void test() {
    ExecutorTest.E e = new ExecutorTest.E();
    RuntimeException re = new RuntimeException();

    String[] v = {null};
    this.executor.execute(() -> v[0] = "1");
    Assert.assertEquals("1", v[0]);

    try {
      this.executor.execute(() -> {throw e;});
    } catch (final Exception ex) {
      Assert.assertSame(e, ex);
    }
    try {
      this.executor.execute(() -> {throw re;});
    } catch (final Exception ex) {
      Assert.assertSame(re, ex);
    }

    Assert.assertEquals("a", this.executor.provide(() -> "a"));

    try {
      this.executor.provide(() -> {throw e;});
    } catch (final Exception ex) {
      Assert.assertSame(e, ex);
    }
    try {
      this.executor.provide(() -> {throw re;});
    } catch (final Exception ex) {
      Assert.assertSame(re, ex);
    }

    this.executor.toRunnable(() -> v[0] = "2").run();
    Assert.assertEquals("2", v[0]);

    try {
      this.executor.toRunnable(() -> {throw e;}).run();
    } catch (final Exception ex) {
      Assert.assertSame(e, ex);
    }
    try {
      this.executor.toRunnable(() -> {throw re;}).run();
    } catch (final Exception ex) {
      Assert.assertSame(re, ex);
    }

    Assert.assertEquals("b", this.executor.toSupplier(() -> "b").get());

    try {
      this.executor.toSupplier(() -> {
        throw e;
      }).get();
    } catch (final Exception ex) {
      Assert.assertSame(e, ex);
    }
    try {
      this.executor.toSupplier(() -> {throw re;}).get();
    } catch (final Exception ex) {
      Assert.assertSame(re, ex);
    }

    this.executor.toConsumer(p -> v[0]=""+p).accept(1);
    Assert.assertEquals("1", v[0]);

    try {
      this.executor.toConsumer(p -> {throw e;}).accept(1);
    } catch (final Exception ex) {
      Assert.assertSame(e, ex);
    }
    try {
      this.executor.toConsumer(p -> {throw re;}).accept(1);
    } catch (final Exception ex) {
      Assert.assertSame(re, ex);
    }

    Assert.assertEquals("1", this.executor.toFunction(p -> "" + p).apply(1));

    try {
      this.executor.toFunction(p -> {
        throw e;
      }).apply(1);
    } catch (final Exception ex) {
      Assert.assertSame(e, ex);
    }
    try {
      this.executor.toFunction(p -> {
        throw re;
      }).apply(1);
    } catch (final Exception ex) {
      Assert.assertSame(re, ex);
    }
  }
  private static class E extends Exception {}
}


