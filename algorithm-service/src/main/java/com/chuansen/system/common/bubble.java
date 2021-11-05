package com.chuansen.system.common;


public class bubble {
    //冒泡算法
    public static void main(String[] args) {
        int[] array = {3, 7, 6, 9, 2, 4, 10, 15, 13};
        for (int i = 0; i < array.length; i++) {
            for (int j = 1; j < array.length - i; j++) {
                if (array[j - 1] > array[j]) {
                    int temp;
                    temp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = temp;
                }
            }
        }
        System.out.println("下标为：" + biSearch(array, 10) + "，数字为:" + array[biSearch(array, 10)]);
        insertSort(array);
        for (int a : array) {
            System.out.println("冒泡排序的值：" + a);
        }
    }

    //二分查找算法   折中算法
    public static int biSearch(int[] array, int a) {
        int start = 0;//查找范围起点
        int end = array.length - 1;//查找范围的终点
        int mid;//查找范围中位数
        while (start <= end) {
            mid = (start + end) / 2;
            //mid=start+(end-start)/2;//取中间
            if (array[mid] == a) {
                return mid;
            } else if (array[mid] < a) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return -1;
    }

    //插入排序算法  针对排好序的数据
    public static void insertSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int insertVal = array[i]; //插入的数
            int index = i - 1; //被插入的位置(准备和前一个数比较)
            while (index >= 0 && insertVal < array[index]) {//如果插入的数比被插入的数小
                array[index + 1] = array[index];//将把 arr[index] 向后移动
                index--; //让index 向前移动
            }
            array[index + 1] = insertVal;
        }
        for (int b : array) {
            System.out.println("插入排序的值：" + b);
        }
    }






}
