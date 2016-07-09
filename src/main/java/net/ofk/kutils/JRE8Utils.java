package net.ofk.kutils;

/**
 * Utilities to make use of JRE8 features
 * which are not exposed by Kotlin runtime.
 */
public class JRE8Utils {
  public static final JRE8Utils INSTANCE = new JRE8Utils();

  private JRE8Utils() {}

  /**
   * Adds an exception to the suppressed exception list of another exception.
   * The latter is an exception that occurs during handling of the former.
   * Only one exception can be thrown, so the former should be added to the list
   * of suppressed exceptions of the latter in such kind of cases.
   */
  public <T extends Exception> T addSuppressed(final T ex, final Exception suppressed) {
    ex.addSuppressed(suppressed);
    return ex;
  }

  /**
   * Returns an array of suppressed exceptions.
   */
  public Throwable[] getSuppressed(final Exception ex) {
    return ex.getSuppressed();
  }
}

