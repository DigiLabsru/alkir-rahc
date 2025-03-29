package ru.digilabs.alkir.rahc.controller.v1;

import com._1c.v8.ibis.admin.IInfoBaseInfo;
import com._1c.v8.ibis.admin.IInfoBaseInfoShort;
import com._1c.v8.ibis.admin.ISessionInfo;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.digilabs.alkir.rahc.controller.v1.api.InfoBaseController;
import ru.digilabs.alkir.rahc.service.RacService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController("infoBaseControllerV1Impl")
@Profile("web")
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer")
@RequestMapping("api/rest/v1/infobase")
public class InfoBaseControllerImpl implements InfoBaseController {

    @Qualifier("sessionScopedRacService")
    private final RacService racService;

    @Override
    @GetMapping("all")
    public List<IInfoBaseInfoShort> list(@RequestParam UUID clusterId) {
        return racService.getInfoBases(clusterId);
    }

    @Override
    @GetMapping("sessions")
    public List<ISessionInfo> sessions(@RequestParam UUID clusterId, @RequestParam UUID ibId) {
        return racService.getSessions(clusterId, ibId);
    }

    @Override
    @GetMapping("all/full")
    public List<IInfoBaseInfo> fullList(@RequestParam UUID clusterId) {
        return racService.getInfoBasesFull(clusterId);
    }

    @Override
    @GetMapping
    public IInfoBaseInfoShort getInfoBase(
        @RequestParam UUID clusterId,
        @RequestParam UUID ibId
    ) {
        return racService.getInfoBase(clusterId, ibId);
    }

    @Override
    @GetMapping("full")
    public IInfoBaseInfo getInfoBaseFull(
        @RequestParam UUID clusterId,
        @RequestParam UUID ibId,
        @RequestParam Optional<String> ibUsername,
        @RequestParam Optional<String> ibPassword
    ) {
        return racService.getInfoBaseFull(clusterId, ibId, ibUsername, ibPassword);
    }

    @Override
    @PutMapping
    public void update(
        @RequestParam UUID clusterId,
        @RequestBody IInfoBaseInfo ibInfo,
        @RequestParam Optional<String> ibUsername,
        @RequestParam Optional<String> ibPassword
    ) {
        racService.updateInfoBase(clusterId, ibInfo, ibUsername, ibPassword);
    }

}
