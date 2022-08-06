package ru.digilabs.alkir.rahc.controller.v2.api;

import com._1c.v8.ibis.admin.IClusterManagerInfo;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.digilabs.alkir.rahc.controller.JsonRpcController;
import ru.digilabs.alkir.rahc.controller.v2.validation.ClusterIdIsNotEmpty;

import javax.validation.Valid;
import java.util.List;

@JsonRpcService("/api/jsonrpc/v2/clusterManager")
@Tag(name = "v2/cluster-manager-controller")
@SecurityRequirement(name = "bearer")
public interface ClusterManagerController extends JsonRpcController {
  @Operation
  List<IClusterManagerInfo> list(
    @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection
  );
}
