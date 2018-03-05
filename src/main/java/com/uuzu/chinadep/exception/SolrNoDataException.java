package com.uuzu.chinadep.exception;

/**
 * solr没有查询到数据
 */
public class SolrNoDataException extends Exception {

    public SolrNoDataException() {
        super("solr没有查询到数据");
    }

    public SolrNoDataException(String message) {
        super(message);
    }

}
