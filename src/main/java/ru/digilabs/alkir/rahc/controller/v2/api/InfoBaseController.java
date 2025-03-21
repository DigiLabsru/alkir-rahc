package ru.digilabs.alkir.rahc.controller.v2.api;

import com._1c.v8.ibis.admin.IInfoBaseInfo;
import com._1c.v8.ibis.admin.IInfoBaseInfoShort;
import com._1c.v8.ibis.admin.ISessionInfo;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ru.digilabs.alkir.rahc.controller.JsonRpcController;
import ru.digilabs.alkir.rahc.controller.v2.validation.ClusterIdIsNotEmpty;
import ru.digilabs.alkir.rahc.controller.v2.validation.InfoBaseIdIsNotEmpty;

import java.util.List;
import java.util.UUID;

@JsonRpcService("/api/jsonrpc/v2/infobase")
@Tag(name = "v2/info-base-controller")
@SecurityRequirement(name = "bearer")
public interface InfoBaseController extends JsonRpcController {

    @Operation
    List<IInfoBaseInfoShort> list(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection
    );

    @Operation
    List<ISessionInfo> sessions(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty @InfoBaseIdIsNotEmpty ConnectionDTO connection
    );

    @Operation
    List<IInfoBaseInfo> fullList(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection
    );

    @Operation
    IInfoBaseInfoShort getInfoBase(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty @InfoBaseIdIsNotEmpty ConnectionDTO connection
    );

    @Operation
    IInfoBaseInfo getInfoBaseFull(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty @InfoBaseIdIsNotEmpty ConnectionDTO connection
    );

    @Operation
    void update(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
        @JsonRpcParam("ibInfo") IInfoBaseInfo ibInfo
    );

    @Operation
    UUID create(
        @JsonRpcParam("connection") @Valid @ClusterIdIsNotEmpty ConnectionDTO connection,
        @JsonRpcParam("ibInfo") IInfoBaseInfo ibInfo,
        @JsonRpcParam("mode") int mode
    );
}
