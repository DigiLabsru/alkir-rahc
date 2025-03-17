package ru.digilabs.alkir.rahc.controller.v2.api;

import com._1c.v8.ibis.admin.IClusterManagerInfo;
import com._1c.v8.ibis.admin.IWorkingProcessInfo;
import com._1c.v8.ibis.admin.IWorkingServerInfo;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ru.digilabs.alkir.rahc.controller.JsonRpcController;
import ru.digilabs.alkir.rahc.controller.v2.validation.ClusterIdIsNotEmpty;

import java.util.List;
import java.util.UUID;

@JsonRpcService("/api/jsonrpc/v2/workingServer")
@Tag(name = "v2/working-server-controller")
public interface WorkingServerController extends JsonRpcController {

    @Operation
    List<IWorkingServerInfo> list(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection
    );

    @Operation
    List<IWorkingProcessInfo> workingProcesses(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
        @JsonRpcParam("serverId") UUID serverId
    );

    @Operation
    List<IClusterManagerInfo> clusterManagers(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
        @JsonRpcParam("serverId") UUID serverId
    );

    @Operation
    IWorkingServerInfo info(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
        @JsonRpcParam("serverId") UUID serverId
    );

    @Operation
    UUID edit(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
        @JsonRpcParam("serverInfo") IWorkingServerInfo serverInfo
    );

    @Operation
    void delete(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
        @JsonRpcParam("serverId") UUID serverId
    );
}
