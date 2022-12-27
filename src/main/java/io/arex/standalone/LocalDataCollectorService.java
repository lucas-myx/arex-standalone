package io.arex.standalone;

import com.google.auto.service.AutoService;
import io.arex.agent.bootstrap.model.ArexMocker;
import io.arex.agent.bootstrap.model.Mocker;
import io.arex.inst.runtime.serializer.Serializer;
import io.arex.inst.runtime.service.DataCollector;
import io.arex.standalone.server.TelnetServer;
import io.arex.standalone.storage.H2StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(DataCollector.class)
public class LocalDataCollectorService implements DataCollector {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2StorageService.class);

    @Override
    public void start() {
        System.out.println("local data collector service start");
        try {
            H2StorageService.INSTANCE.start();
            TelnetServer.INSTANCE.start();
        } catch (Exception e) {
            LOGGER.warn("local data collector service start error", e);
        }
    }

    @Override
    public void save(String json) {
        System.out.println("local data collector service save:"+json);
        H2StorageService.INSTANCE.save(Serializer.deserialize(json, ArexMocker.class), json);
    }

    @Override
    public String query(String json) {
        System.out.println("local data collector service query:"+json);
        Mocker mocker = Serializer.deserialize(json, ArexMocker.class);
        H2StorageService.INSTANCE.save(mocker, json);
        mocker.setReplayId(null);
        String result = H2StorageService.INSTANCE.queryJson(mocker);
        System.out.println("local data collector service query result:"+result);
        return result;
    }
}
