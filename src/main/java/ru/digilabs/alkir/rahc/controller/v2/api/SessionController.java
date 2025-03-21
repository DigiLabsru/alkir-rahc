package ru.digilabs.alkir.rahc.controller.v2.api;

import com._1c.v8.ibis.admin.ISessionInfo;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ru.digilabs.alkir.rahc.controller.JsonRpcController;
import ru.digilabs.alkir.rahc.controller.v2.validation.ClusterIdIsNotEmpty;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@JsonRpcService("/api/jsonrpc/v2/session")
@Tag(name = "v2/session-controller")
public interface SessionController extends JsonRpcController {

    @Operation
    List<ISessionInfo> list(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection
    );

    @Operation
    ISessionInfo info(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
        @JsonRpcParam("sid") UUID sid
    );

    @Operation
    void delete(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
        @JsonRpcParam("sid") UUID sid,
        @JsonRpcParam("message") Optional<String> message
    );
}
