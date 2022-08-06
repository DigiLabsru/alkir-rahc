package ru.digilabs.alkir.rahc.controller.v2.api;

import com._1c.v8.ibis.admin.IWorkingProcessInfo;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.digilabs.alkir.rahc.controller.JsonRpcController;
import ru.digilabs.alkir.rahc.controller.v2.validation.ClusterIdIsNotEmpty;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@JsonRpcService("/api/jsonrpc/v2/workingProcess")
@Tag(name = "v2/working-process-controller")
public interface WorkingProcessController extends JsonRpcController {

  @Operation
  List<IWorkingProcessInfo> list(
    @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection
  );

  @Operation
  IWorkingProcessInfo info(
    @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
    @JsonRpcParam("processId") UUID processId
  );

}
