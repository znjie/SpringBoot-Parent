package com.chuansen.system.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SystemLog {
    private String username;
    private String userId;
    private String state;

}
