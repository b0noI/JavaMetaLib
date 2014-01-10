package com.java.meta;

/**
 * This class contains checkRecursion methods, can't be instantiated due to all methods is static
 */
public final class RecursionController {

    /**
     * see RecursionController.checkRecursion
     *
     * @param maxTimes
     * @throws RecursionInvocationException
     */
    public static void checkRecursion(final int maxTimes) throws RecursionInvocationException {
        checkRecursion(maxTimes, false);
    }

    /**
     * Checking recursions in stack. If method was called more than {maxTimes} times throws exception RecursionInvocationException
     *
     * For Example:
     *
     * {@code "static final int MAX_RECURSION_CALL = 3;
     * boolean get(){
     * RecursionController.checkRecursion(MAX_RECURSION_CALL, false);
     * // source
     * if (!result) return get();
     * }"}
     *
     * if methods stack will be like:
     * get()
     * foo()
     * get()
     * foo()
     * foo()
     *
     * it will NOT throw exception, but in next case:
     *
     * get()
     * foo()
     * get()
     * get()
     * foo()
     * get()
     * foo()
     * foo()
     *
     * it will throw exception (if inLine is false). So you can start method like:
     *
     * try {
     *     get();
     * } catch(RecursionInvocationException e) {
     *     // code
     * }
     *
     * next case will throw Exception even if inLIne if true:
     *
     * get()
     * get()
     * get()
     * get()
     * foo()
     *
     * @param maxTimes
     * @throws RecursionInvocationException
     */
    public static void checkRecursion(final int maxTimes, final boolean inLine) throws RecursionInvocationException {
        if (maxTimes <= 0)
            throw new IllegalArgumentException(TAG + " maxTimes <= 0");

        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        if (stackTraceElements.length <= 1)
            return;

        if (!inLine) {
            if (RecursionController.countElementInArray(stackTraceElements, 1) > maxTimes) {
                failDueRecursionException(stackTraceElements, maxTimes);
            }
        } else {
            if (RecursionController.countElementInArrayInRow(stackTraceElements, 1, 1) > maxTimes) {
                failDueRecursionException(stackTraceElements, maxTimes);
            }
        }
    }

    /**
     * RecursionInvocationException throwing by methods in this class in case of unwanted recursion
     */
    public static class RecursionInvocationException extends Exception {

        private RecursionInvocationException(final String msg) {
            super(msg);
        }

    }

    // Private section

    private static final String TAG = RecursionController.class.getSimpleName();

    private static final String METHOD_INVOCATION_EXCEPTION_TEXT = "Method %s called more than %i times";

    private static void failDueRecursionException(final StackTraceElement[] stackTraceElements, final int maxTimes) throws RecursionInvocationException {
        Thread.dumpStack();
        String exceptionString = String.format(METHOD_INVOCATION_EXCEPTION_TEXT, stackTraceElements[1].toString(), maxTimes);
        throw new RecursionInvocationException(exceptionString);
    }

    private static<T> int countElementInArray(final T[] array, final int index) {
        int count = 0;
        final T targetElement = array[index];
        for (final T element : array) {
            if (element.equals(targetElement))
                count++;
        }
        return count;
    }

    private static<T> int countElementInArrayInRow(final T[] array, final int startIndex, final int index) {
        int count = 0;
        final T targetElement = array[index];
        for (int i = startIndex; i < array.length; i++) {
            final T element = array[i];
            if (element.equals(targetElement))
                count++;
            else
                return count;
        }
        return count;
    }

    private RecursionController(){}

}
