package ru.digilabs.alkir.rahc.controller.v1.api;

import com._1c.v8.ibis.admin.IWorkingProcessInfo;
import ru.digilabs.alkir.rahc.controller.JsonRpcController;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@JsonRpcService("/api/jsonrpc/v1/workingProcess")
@Tag(name = "v1/working-process-controller")
public interface WorkingProcessController extends JsonRpcController {

  @Operation
  List<IWorkingProcessInfo> list(
    @JsonRpcParam("clusterId") UUID clusterId
  );

  @Operation
  IWorkingProcessInfo info(
    @JsonRpcParam("clusterId") UUID clusterId,
    @JsonRpcParam("processId") UUID processId
  );

}
