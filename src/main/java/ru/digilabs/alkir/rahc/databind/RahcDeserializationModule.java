package ru.digilabs.alkir.rahc.databind;

import com._1c.v8.ibis.admin.ClusterInfo;
import com._1c.v8.ibis.admin.IClusterInfo;
import com._1c.v8.ibis.admin.IInfoBaseInfo;
import com._1c.v8.ibis.admin.IPortRangeInfo;
import com._1c.v8.ibis.admin.IWorkingServerInfo;
import com._1c.v8.ibis.admin.InfoBaseInfo;
import com._1c.v8.ibis.admin.WorkingServerInfo;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

@Component
public class RahcDeserializationModule extends SimpleModule {
  @Override
  public void setupModule(SetupContext context) {
    addDeserializer(IWorkingServerInfo.class, new GenericDeserializer<>(IWorkingServerInfo.class, WorkingServerInfo.class));
    addDeserializer(IInfoBaseInfo.class, new GenericDeserializer<>(IInfoBaseInfo.class, InfoBaseInfo.class));
    addDeserializer(IClusterInfo.class, new GenericDeserializer<>(IClusterInfo.class, ClusterInfo.class));
    addDeserializer(IPortRangeInfo.class, new IPortRangeInfoDeserializer(IPortRangeInfo.class));

    super.setupModule(context);
  }
}
