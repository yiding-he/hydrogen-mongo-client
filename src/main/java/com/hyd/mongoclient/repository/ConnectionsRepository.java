package com.hyd.mongoclient.repository;

import com.hyd.mongoclient.domain.ConnectionInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConnectionsRepository {

    private final List<ConnectionInfo> list = new ArrayList<>();

    public List<ConnectionInfo> all() {
        return list;
    }

    public void add(ConnectionInfo connectionInfo) {
        list.add(connectionInfo);
    }

    public void delete(int index) {
        if (index >= 0 && index < list.size()) {
            list.remove(index);
        }
    }

    public void update(int index, ConnectionInfo connectionInfo) {
        if (index >= 0 && index < list.size()) {
            list.set(index, connectionInfo);
        }
    }
}
