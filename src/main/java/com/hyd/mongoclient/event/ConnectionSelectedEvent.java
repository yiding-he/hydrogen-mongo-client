package com.hyd.mongoclient.event;

import com.hyd.mongoclient.domain.ConnectionInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ConnectionSelectedEvent {

    private final ConnectionInfo connectionInfo;
}
