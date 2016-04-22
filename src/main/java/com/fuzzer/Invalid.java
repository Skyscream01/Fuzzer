package com.fuzzer;

/**
 * Created by yauhen.bialiayeu on 22.04.2016.
 */
public class Invalid extends Exception {
    public Invalid() { super(); }
    public Invalid(String message) { super(message); }
    public Invalid(String message, Throwable cause) { super(message, cause); }
    public Invalid(Throwable cause) { super(cause); }
}
