package ru.digilabs.alkir.rahc.databind;

import com._1c.v8.ibis.admin.IPortRangeInfo;
import com._1c.v8.ibis.admin.PortRangeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

import java.io.IOException;

public class IPortRangeInfoDeserializer extends StdDeserializer<IPortRangeInfo> {

    public IPortRangeInfoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public IPortRangeInfo deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        var node = jp.getCodec().readTree(jp);
        int highBound = (Integer) ((IntNode) node.get("highBound")).numberValue();
        int lowBound = (Integer) ((IntNode) node.get("lowBound")).numberValue();
        return new PortRangeInfo(highBound, lowBound);
    }
}
