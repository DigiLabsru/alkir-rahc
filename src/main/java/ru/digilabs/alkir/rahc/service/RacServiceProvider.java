package ru.digilabs.alkir.rahc.service;

import com._1c.v8.ibis.admin.client.IAgentAdminConnectorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.digilabs.alkir.rahc.configuration.RetryableRacMethod;
import ru.digilabs.alkir.rahc.dto.ConnectionDTO;

@Service
@RequiredArgsConstructor
public class RacServiceProvider {

    @Qualifier("racService")
    private final ObjectProvider<RacService> racServiceObjectProvider;
    private final IAgentAdminConnectorFactory factory;

    @RetryableRacMethod
    public RacService getRacService(ConnectionDTO rasConnection) {
        var configurationProperties = rasConnection.toConfigurationProperties();
        return racServiceObjectProvider.getObject(configurationProperties, factory);
    }
}
