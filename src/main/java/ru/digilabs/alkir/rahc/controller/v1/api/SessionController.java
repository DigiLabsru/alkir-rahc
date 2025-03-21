package ru.digilabs.alkir.rahc.controller.v1.api;

import com._1c.v8.ibis.admin.ISessionInfo;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.digilabs.alkir.rahc.controller.JsonRpcController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@JsonRpcService("/api/jsonrpc/v1/session")
@Tag(name = "v1/session-controller")
public interface SessionController extends JsonRpcController {

    @Operation
    List<ISessionInfo> list(@JsonRpcParam("clusterId") UUID clusterId);

    @Operation
    ISessionInfo info(
        @JsonRpcParam("clusterId") UUID clusterId,
        @JsonRpcParam("sid") UUID sid
    );

    @Operation
    void delete(
        @JsonRpcParam("clusterId") UUID clusterId,
        @JsonRpcParam("sid") UUID sid,
        @JsonRpcParam("message") Optional<String> message
    );
}
