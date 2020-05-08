package com.exception;

import java.util.SplittableRandom;

/**
 * @author false
 * @date 20/4/17 17:57
 */
public class killException extends RuntimeException {
    public killException(String message){
        super(message);
    }
}
