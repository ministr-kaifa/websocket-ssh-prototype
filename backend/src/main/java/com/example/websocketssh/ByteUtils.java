package com.example.websocketssh;

import java.util.Arrays;

public class ByteUtils {

  private ByteUtils() {

  }
  
  public static Byte[] boxed(byte[] primitiveArray) {
    var result = new Byte[primitiveArray.length];
    Arrays.setAll(result, i -> primitiveArray[i]);
    return result;
  }

  public static byte[] unboxed(Byte[] boxedArray) {
    var result = new byte[boxedArray.length];
    for (int i = 0; i < boxedArray.length; i++) {
      result[i] = boxedArray[i];
    }
    return result;
  }
}
