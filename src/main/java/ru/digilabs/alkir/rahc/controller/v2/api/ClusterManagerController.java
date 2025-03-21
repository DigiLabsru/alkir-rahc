package ru.digilabs.alkir.rahc.controller.v2.api;

import com._1c.v8.ibis.admin.IClusterManagerInfo;
import com._1c.v8.ibis.admin.IClusterServiceInfo;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ru.digilabs.alkir.rahc.controller.JsonRpcController;
import ru.digilabs.alkir.rahc.controller.v2.validation.ClusterIdIsNotEmpty;

import java.util.List;
import java.util.UUID;

@JsonRpcService("/api/jsonrpc/v2/clusterManager")
@Tag(name = "v2/cluster-manager-controller")
@SecurityRequirement(name = "bearer")
public interface ClusterManagerController extends JsonRpcController {
    @Operation
    List<IClusterManagerInfo> list(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection
    );

    @Operation
    IClusterManagerInfo getClusterManagerInfo(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
        @JsonRpcParam("managerId") UUID managerId
    );

    @Operation
    List<IClusterServiceInfo> getClusterServices(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
        @JsonRpcParam("managerId") UUID managerId
    );
}
