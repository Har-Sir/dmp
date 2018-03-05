package com.uuzu.chinadep.exception;

/**
 * hbase没有查询到数据
 */
public class HBaseNoDataException extends Exception {

    public HBaseNoDataException() {
        super("hbase没有查询到数据");
    }

    public HBaseNoDataException(String message) {
        super(message);
    }

}
