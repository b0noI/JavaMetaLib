package com.java.meta;

/**
 * Created by b0noI on 08/01/2014.
 */
public class RecursionController {

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

}
