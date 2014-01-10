package com.java.meta;

import org.junit.Test;

public class RecursionControllerTest {
    public RecursionController recursionController = new RecursionController();

    public void testCheckRecursionMethodNullPointerExeption(int x) throws Exception{
        for (int i = x; i >= 0; i--){
            x--;
            recursionController.checkRecursion(x, true);
        }
        this.testCheckRecursionMethodNullPointerExeption(x);
    }

    public void testCheckRecursionMethodStackOverFlow(int x) throws Exception{
        for (int i = x; i > 1; i--){
            x--;
            recursionController.checkRecursion(3, true);
        }
        this.testCheckRecursionMethodStackOverFlow(x);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithOutCheckRecursionNullPointerTrue() throws Exception {
        RecursionController recursionController = new RecursionController();
        recursionController.checkRecursion(0, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithOutCheckRecursionNullPointerFalse() throws Exception {
        RecursionController recursionController = new RecursionController();
        recursionController.checkRecursion(0, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckRecursionNullPointerZerro() throws Exception {
        this.testCheckRecursionMethodNullPointerExeption(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckRecursionNullPointerHundred() throws Exception {
        this.testCheckRecursionMethodNullPointerExeption(100);
    }

    @Test(expected = StackOverflowError.class)
    public void testCheckRecursionStackOverFlowZerro() throws Exception {
        this.testCheckRecursionMethodStackOverFlow(0);
    }

    @Test(expected = StackOverflowError.class)
    public void testCheckRecursionStackOverFlowHundred() throws Exception {
        this.testCheckRecursionMethodStackOverFlow(100);
    }






}
