package com.mutisource.bigdata.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeremy
 * @create 2020 07 23 17:13
 * 简单测试类
 *
 */
public class SimpleTest {

    public static void main(String[] args) {
        List<String> testList = new ArrayList<>();

        testList.add("v1");
        testList.add("v2");
        testList.add("v3");
        testList.add("v4");
        testList.add("v5");
        System.out.println(testList.get(0));


    }
}
