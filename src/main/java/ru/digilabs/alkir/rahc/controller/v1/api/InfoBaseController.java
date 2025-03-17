package ru.digilabs.alkir.rahc.controller.v1.api;

import com._1c.v8.ibis.admin.IInfoBaseInfo;
import com._1c.v8.ibis.admin.IInfoBaseInfoShort;
import com._1c.v8.ibis.admin.ISessionInfo;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.digilabs.alkir.rahc.controller.JsonRpcController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@JsonRpcService("/api/jsonrpc/v1/infobase")
@Tag(name = "v1/info-base-controller")
@SecurityRequirement(name = "bearer")
public interface InfoBaseController extends JsonRpcController {

    @Operation
    List<IInfoBaseInfoShort> list(@JsonRpcParam("clusterId") UUID clusterId);

    @Operation
    List<ISessionInfo> sessions(
        @JsonRpcParam("clusterId") UUID clusterId,
        @JsonRpcParam("ibId") UUID ibId
    );

    @Operation
    List<IInfoBaseInfo> fullList(@JsonRpcParam("clusterId") UUID clusterId);

    @Operation
    IInfoBaseInfoShort getInfoBase(
        @JsonRpcParam("clusterId") UUID clusterId,
        @JsonRpcParam("ibId") UUID ibId
    );

    @Operation
    IInfoBaseInfo getInfoBaseFull(
        @JsonRpcParam("clusterId") UUID clusterId,
        @JsonRpcParam("ibId") UUID ibId,
        @JsonRpcParam("ibUsername") Optional<String> ibUsername,
        @JsonRpcParam("ibPassword") Optional<String> ibPassword
    );

    @Operation
    void update(
        @JsonRpcParam("clusterId") UUID clusterId,
        @JsonRpcParam("ibInfo") IInfoBaseInfo ibInfo,
        @JsonRpcParam("ibUsername") Optional<String> ibUsername,
        @JsonRpcParam("ibPassword") Optional<String> ibPassword
    );
}
