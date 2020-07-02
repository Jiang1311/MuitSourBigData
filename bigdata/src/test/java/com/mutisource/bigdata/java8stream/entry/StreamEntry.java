package com.mutisource.bigdata.java8stream.entry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author Jeremy
 * @create 2020 07 02 23:04
 */
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class StreamEntry {

    private int intType;

    private float floatType;

    private long longType;

    private double doubleType;

    private boolean booleanType;

    private String stringType;




}
