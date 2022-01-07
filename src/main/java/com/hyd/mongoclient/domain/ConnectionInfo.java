package com.hyd.mongoclient.domain;

import lombok.Data;

@Data
public class ConnectionInfo {

    private String name;

    private String url;

    private String username;

    private String password;
}
