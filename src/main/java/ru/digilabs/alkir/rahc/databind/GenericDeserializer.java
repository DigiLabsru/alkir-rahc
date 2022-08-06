package ru.digilabs.alkir.rahc.databind;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class GenericDeserializer<T> extends StdDeserializer<T> {

  private final Class<?> implClass;

  public GenericDeserializer(Class<? extends T> vc, Class<?> implClass) {
    super(vc);
    this.implClass = implClass;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return (T) p.readValueAs(implClass);
  }

}
