package ru.digilabs.alkir.rahc.controller.v1.api;

import com._1c.v8.ibis.admin.IClusterManagerInfo;
import com._1c.v8.ibis.admin.IWorkingProcessInfo;
import com._1c.v8.ibis.admin.IWorkingServerInfo;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.digilabs.alkir.rahc.controller.JsonRpcController;

import java.util.List;
import java.util.UUID;

@JsonRpcService("/api/jsonrpc/v1/workingServer")
@Tag(name = "v1/working-server-controller")
public interface WorkingServerController extends JsonRpcController {

  @Operation
  List<IWorkingServerInfo> list(
    @JsonRpcParam("clusterId") UUID clusterId
  );

  @Operation
  List<IWorkingProcessInfo> workingProcesses(
    @JsonRpcParam("clusterId") UUID clusterId,
    @JsonRpcParam("serverId") UUID serverId
  );

  @Operation
  List<IClusterManagerInfo> clusterManagers(
    @JsonRpcParam("clusterId") UUID clusterId,
    @JsonRpcParam("serverId") UUID serverId
  );

  @Operation
  IWorkingServerInfo info(
    @JsonRpcParam("clusterId") UUID clusterId,
    @JsonRpcParam("serverId") UUID serverId
  );

  @Operation
  UUID edit(
    @JsonRpcParam("clusterId") UUID clusterId,
    @JsonRpcParam("serverInfo") IWorkingServerInfo serverInfo
  );

  @Operation
  void delete(
    @JsonRpcParam("clusterId") UUID clusterId,
    @JsonRpcParam("serverId") UUID serverId
  );
}
