package ru.digilabs.alkir.rahc.controller.v2.api;

import com._1c.v8.ibis.admin.IClusterInfo;
import com._1c.v8.ibis.admin.IClusterServiceInfo;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ru.digilabs.alkir.rahc.controller.JsonRpcController;
import ru.digilabs.alkir.rahc.controller.v2.validation.ClusterIdIsNotEmpty;
import ru.digilabs.alkir.rahc.dto.ConnectionDTO;

import java.util.List;
import java.util.UUID;

@JsonRpcService("/api/jsonrpc/v2/cluster")
@Tag(name = "v2/cluster-controller")
@SecurityRequirement(name = "bearer")
public interface ClusterController extends JsonRpcController {
    @Operation
    List<IClusterInfo> list(
        @JsonRpcParam("connection") @Valid ConnectionDTO connection
    );

    @Operation
    UUID edit(
        @JsonRpcParam("connection") @Valid ConnectionDTO connection,
        @JsonRpcParam("clusterInfo") IClusterInfo clusterInfo
    );

    @Operation
    List<IClusterServiceInfo> getClusterServices(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection
    );
}
