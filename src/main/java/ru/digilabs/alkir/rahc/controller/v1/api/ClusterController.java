package ru.digilabs.alkir.rahc.controller.v1.api;

import com._1c.v8.ibis.admin.IClusterInfo;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.digilabs.alkir.rahc.controller.JsonRpcController;

import java.util.List;
import java.util.UUID;

@JsonRpcService("/api/jsonrpc/v1/cluster")
@Tag(name = "v1/cluster-controller")
@SecurityRequirement(name = "bearer")
public interface ClusterController extends JsonRpcController {
    @Operation
    List<IClusterInfo> list();

    @Operation
    UUID edit(@JsonRpcParam("clusterInfo") IClusterInfo clusterInfo);
}
