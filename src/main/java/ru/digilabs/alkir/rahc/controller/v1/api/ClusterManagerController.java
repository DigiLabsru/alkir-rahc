package ru.digilabs.alkir.rahc.controller.v1.api;

import com._1c.v8.ibis.admin.IClusterManagerInfo;
import ru.digilabs.alkir.rahc.controller.JsonRpcController;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@JsonRpcService("/api/jsonrpc/v1/clusterManager")
@Tag(name = "v1/cluster-manager-controller")
@SecurityRequirement(name = "bearer")
public interface ClusterManagerController extends JsonRpcController {
  @Operation
  List<IClusterManagerInfo> list(@JsonRpcParam("clusterId") UUID clusterId);
}
