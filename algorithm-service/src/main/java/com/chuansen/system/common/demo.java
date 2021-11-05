package com.chuansen.system.common;

public class demo {
    public static void main(String[] args) {
            int[] array = {3, 7, 6, 9, 2, 4, 10, 15, 13};
            for (int i = 0; i < array.length; i++) {
                for (int j = 1; j < array.length - i; j++) {
                    if (array[j ] > array[j+1]) {
                        int temp;
                        temp = array[j];
                        array[j ] = array[j+1];
                        array[j+1] = temp;
                    }
                }
            }
            for (int a : array) {
                System.out.println("冒泡排序的值：" + a);
            }



    }

    public static void reverse(int a[], int n){
        int[] b = new int[n];
        int j = n;
        for (int i = 0; i < n; i++) {
            b[j - 1] = a[i];
            j = j - 1;
        }
    }

}
