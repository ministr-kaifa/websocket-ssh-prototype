package com.example.websocketssh;

import java.util.Map;

public record Stand(long id, String address, Map<Long, Shell> shells) {
  
}
