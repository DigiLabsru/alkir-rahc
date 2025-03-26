package ru.digilabs.alkir.rahc.service;

import com._1c.v8.ibis.admin.AgentAdminAuthenticationException;
import com._1c.v8.ibis.admin.AgentAdminException;
import com._1c.v8.ibis.admin.IAgentAdminConnection;
import com._1c.v8.ibis.admin.IClusterInfo;
import com._1c.v8.ibis.admin.IClusterManagerInfo;
import com._1c.v8.ibis.admin.IClusterServiceInfo;
import com._1c.v8.ibis.admin.IInfoBaseInfo;
import com._1c.v8.ibis.admin.IInfoBaseInfoShort;
import com._1c.v8.ibis.admin.ISessionInfo;
import com._1c.v8.ibis.admin.IWorkingProcessInfo;
import com._1c.v8.ibis.admin.IWorkingServerInfo;
import com._1c.v8.ibis.admin.client.IAgentAdminConnector;
import com._1c.v8.ibis.admin.client.IAgentAdminConnectorFactory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.digilabs.alkir.rahc.configuration.RasConfigurationProperties;
import ru.digilabs.alkir.rahc.configuration.RetryableRacMethod;
import ru.digilabs.alkir.rahc.dto.ConnectionDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Service
@Scope(value = SCOPE_PROTOTYPE)
@Slf4j
@RequiredArgsConstructor
public class RacService implements Serializable, AutoCloseable {

    private final transient RasConfigurationProperties rasProperties;

    @Autowired
    private transient IAgentAdminConnectorFactory factory;

    private transient IAgentAdminConnector connector;
    private transient IAgentAdminConnection connection;

    @PostConstruct
    protected void init() {
        connector = factory.createConnector(rasProperties.getTimeout());

    }

    @PreDestroy
    protected void shutdown() {
        connector.shutdown();
    }

    @Override
    public void close() {
        shutdown();
    }

    @RetryableRacMethod
    public UUID getClusterId(ConnectionDTO connection) {
        checkConnection();
        if (connection.getClusterId().isPresent()) {
            return UUID.fromString(connection.getClusterId().orElseThrow());
        }

        var clusterName = connection.getClusterName().orElseThrow();
        return getClusters().stream()
            .filter(clusterInfo -> clusterInfo.getName().equalsIgnoreCase(clusterName))
            .findAny()
            .map(IClusterInfo::getClusterId)
            .orElseThrow(() -> new IllegalArgumentException("Can't find cluster with name %s".formatted(clusterName)));
    }

    @RetryableRacMethod
    public UUID getIbId(ConnectionDTO connection) {
        checkConnection();
        if (connection.getIbId().isPresent()) {
            return UUID.fromString(connection.getIbId().orElseThrow());
        }

        var clusterId = getClusterId(connection);
        var ibName = connection.getIbName().orElseThrow();
        return getInfoBases(clusterId).stream()
            .filter(clusterInfo -> clusterInfo.getName().equalsIgnoreCase(ibName))
            .findAny()
            .map(IInfoBaseInfoShort::getInfoBaseId)
            .orElseThrow(() -> new IllegalArgumentException("Can't find infobase with name %s".formatted(ibName)));
    }

    /**
     * Retrieves all clusters from the remote administration service.
     * <p>
     * This method verifies the connection state before fetching cluster information.
     * </p>
     *
     * @return a list of {@link IClusterInfo} objects representing the available clusters
     */
    @RetryableRacMethod
    public List<IClusterInfo> getClusters() {
        checkConnection();
        return connection.getClusters();
    }

    /**
     * Retrieves detailed information about the cluster identified by the specified UUID.
     *
     * <p>This method verifies an active connection with the remote administration service and then
     * returns the comprehensive information associated with the given cluster identifier.</p>
     *
     * @param clusterId the unique identifier of the cluster
     * @return the detailed information of the specified cluster
     */
    @RetryableRacMethod
    public IClusterInfo getClusterInfo(UUID clusterId) {
        checkConnection();
        return connection.getClusterInfo(clusterId);
    }

    /**
     * Edits a cluster's information by authenticating and updating its record in the remote administration service.
     *
     * <p>This method first confirms an active connection, then performs authentication using the cluster identifier
     * from the provided cluster information. It then submits the updated cluster details to the remote service,
     * returning the unique identifier corresponding to the edited cluster.
     *
     * @param clusterInfo the updated cluster information
     * @return the unique identifier of the edited cluster
     */
    @RetryableRacMethod
    public UUID editCluster(IClusterInfo clusterInfo) {
        checkConnection();
        authenticate(clusterInfo.getClusterId());
        return connection.regCluster(clusterInfo);
    }

    @RetryableRacMethod
    public List<IClusterManagerInfo> getClusterManagers(UUID clusterId) {
        checkConnection();
        authenticate(clusterId);
        return connection.getClusterManagers(clusterId);
    }

    @RetryableRacMethod
    public IClusterManagerInfo getClusterManagerInfo(UUID clusterId, UUID managerId) {
        checkConnection();
        authenticate(clusterId);
        return connection.getClusterManagerInfo(clusterId, managerId);
    }

    @RetryableRacMethod
    public List<IClusterServiceInfo> getClusterServices(UUID clusterId) {
        checkConnection();
        authenticate(clusterId);
        return connection.getClusterServices(clusterId);
    }

    @RetryableRacMethod
    public List<IClusterServiceInfo> getClusterServices(UUID clusterId, UUID managerId) {
        checkConnection();
        authenticate(clusterId);

        return getClusterServices(clusterId).stream()
            .filter(service -> service.getClusterManagerIds().contains(managerId))
            .toList();
    }

    @RetryableRacMethod
    public List<IInfoBaseInfo> getInfoBasesFull(UUID clusterId) {
        checkConnection();
        authenticate(clusterId);
        return connection.getInfoBases(clusterId);
    }

    @RetryableRacMethod
    public List<IInfoBaseInfoShort> getInfoBases(UUID clusterId) {
        checkConnection();
        authenticate(clusterId);
        return connection.getInfoBasesShort(clusterId);
    }

    @RetryableRacMethod
    public IInfoBaseInfoShort getInfoBase(
        UUID clusterId,
        UUID ibId
    ) {
        checkConnection();
        authenticate(clusterId);
        return connection.getInfoBaseShortInfo(clusterId, ibId);
    }

    @RetryableRacMethod
    public IInfoBaseInfo getInfoBaseFull(
        UUID clusterId,
        UUID ibId,
        Optional<String> ibUsername,
        Optional<String> ibPassword
    ) {
        checkConnection();
        authenticate(clusterId);
        authenticateInfoBase(clusterId, ibUsername, ibPassword);

        return connection.getInfoBaseInfo(clusterId, ibId);
    }

    @RetryableRacMethod
    public void updateInfoBase(
        UUID clusterId,
        IInfoBaseInfo ibInfo,
        Optional<String> ibUsername,
        Optional<String> ibPassword
    ) throws IllegalArgumentException {
        checkConnection();
        authenticate(clusterId);
        authenticateInfoBase(clusterId, ibUsername, ibPassword);

        // todo: Проверить, возможно этот символ обрабатывается внутри RAS.
        if ("�".equals(ibInfo.getDbPassword())) {
            throw new IllegalArgumentException("ibInfo must contain actual database password.");
        }
        connection.updateInfoBase(clusterId, ibInfo);
    }

    @RetryableRacMethod
    public UUID createInfoBase(
        UUID clusterId,
        IInfoBaseInfo ibInfo,
        int mode
    ) throws IllegalArgumentException {
        checkConnection();
        authenticate(clusterId);

        return connection.createInfoBase(clusterId, ibInfo, mode);
    }

    @RetryableRacMethod
    public List<ISessionInfo> getSessions(UUID clusterId) {
        checkConnection();
        authenticate(clusterId);
        return connection.getSessions(clusterId);
    }

    @RetryableRacMethod
    public List<ISessionInfo> getSessions(
        UUID clusterId,
        UUID ibId
    ) {
        checkConnection();
        authenticate(clusterId);

        return getSessions(clusterId).stream()
            .filter(sessionInfo -> sessionInfo.getInfoBaseId().equals(ibId))
            .toList();
    }

    @RetryableRacMethod
    public ISessionInfo getSessionInfo(UUID clusterId, UUID sid) {
        checkConnection();
        authenticate(clusterId);
        return connection.getSessionInfo(clusterId, sid);
    }

    @RetryableRacMethod
    public void terminateSession(UUID clusterId, UUID sid, Optional<String> message) {
        checkConnection();
        authenticate(clusterId);
        message.ifPresentOrElse(
            messageText -> connection.terminateSession(clusterId, sid, messageText),
            () -> connection.terminateSession(clusterId, sid)
        );
    }

    @RetryableRacMethod
    public List<IWorkingServerInfo> getWorkingServers(UUID clusterId) {
        checkConnection();
        authenticate(clusterId);
        return connection.getWorkingServers(clusterId);
    }

    @RetryableRacMethod
    public IWorkingServerInfo getWorkingServer(UUID clusterId, UUID serverId) {
        checkConnection();
        authenticate(clusterId);
        return connection.getWorkingServerInfo(clusterId, serverId);
    }

    @RetryableRacMethod
    public UUID editWorkingServer(UUID clusterId, IWorkingServerInfo serverInfo) {
        checkConnection();
        authenticate(clusterId);
        return connection.regWorkingServer(clusterId, serverInfo);
    }

    @RetryableRacMethod
    public void deleteWorkingServer(UUID clusterId, UUID serverId) {
        checkConnection();
        authenticate(clusterId);
        connection.unregWorkingServer(clusterId, serverId);
    }

    @RetryableRacMethod
    public List<IWorkingProcessInfo> getWorkingProcesses(UUID clusterId) {
        checkConnection();
        authenticate(clusterId);
        return connection.getWorkingProcesses(clusterId);
    }

    @RetryableRacMethod
    public IWorkingProcessInfo getWorkingProcess(UUID clusterId, UUID processId) {
        checkConnection();
        authenticate(clusterId);
        return connection.getWorkingProcessInfo(clusterId, processId);
    }

    @RetryableRacMethod
    public List<IWorkingProcessInfo> getServerWorkingProcesses(UUID clusterId, UUID serverId) {
        checkConnection();
        authenticate(clusterId);
        return connection.getServerWorkingProcesses(clusterId, serverId);
    }

    @RetryableRacMethod
    public List<IClusterManagerInfo> getServerClusterManagers(UUID clusterId, UUID serverId) {
        checkConnection();
        authenticate(clusterId);

        var workingServer = getWorkingServer(clusterId, serverId);
        var hostName = workingServer.getHostName();
        return getClusterManagers(clusterId).stream()
            .filter(clusterManagerInfo -> clusterManagerInfo.getHostName().equalsIgnoreCase(hostName))
            .toList();
    }

    private void authenticate(UUID clusterId) {
        try {
            connection.getClusterAdmins(clusterId);
        } catch (AgentAdminAuthenticationException ex) {
            doAuthenticate(clusterId);
        }
    }

    private void authenticateInfoBase(UUID clusterId, Optional<String> ibUsername, Optional<String> ibPassword) {
        ibUsername.ifPresent(username -> connection.addAuthentication(clusterId, username, ibPassword.orElse("")));
    }

    private void doAuthenticate(UUID clusterId) {
        var clusterAdminUsername = rasProperties.getClusterAdminUsername();
        var clusterAdminPassword = rasProperties.getClusterAdminPassword();
        connection.authenticate(clusterId, clusterAdminUsername, clusterAdminPassword);
    }

    private void checkConnection() {
        if (connection == null) {
            doCreateConnection();
        }

        try {
            connection.getClusters();
        } catch (AgentAdminException ex) {
            // try to reconnect
            LOGGER.warn("Can't get clusters from connection. Trying to reconnect to RAS...", ex);

            doCreateConnection();
        }
    }

    private synchronized void doCreateConnection() {
        connection = connector.connect(rasProperties.getAddress(), rasProperties.getPort());

        connection.authenticateAgent(
            rasProperties.getCentralServerAdminUsername(),
            rasProperties.getCentralServerAdminPassword()
        );
    }
}
