/*
 * Jooby https://jooby.io
 * Apache License Version 2.0 https://jooby.io/LICENSE.txt
 * Copyright 2014 Edgar Espina
 */
package io.jooby.weld;

import java.lang.annotation.Annotation;

import org.jboss.weld.environment.se.WeldContainer;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.jooby.Registry;
import io.jooby.ServiceKey;
import io.jooby.exception.RegistryException;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.literal.NamedLiteral;

class WeldRegistry implements Registry {

  private WeldContainer container;

  WeldRegistry(WeldContainer container) {
    this.container = container;
  }

  @NonNull @Override
  public <T> T require(@NonNull Class<T> type) {
    return require(ServiceKey.key(type));
  }

  @NonNull @Override
  public <T> T require(@NonNull Class<T> type, @NonNull String name) {
    return require(ServiceKey.key(type, name));
  }

  @NonNull @Override
  public <T> T require(ServiceKey<T> key) {
    try {
      return container.select(key.getType(), literal(key)).get();
    } catch (Exception cause) {
      throw new RegistryException("Provisioning of `" + key + "` resulted in exception", cause);
    }
  }

  private Annotation literal(ServiceKey key) {
    String name = key.getName();
    return name == null ? Any.Literal.INSTANCE : NamedLiteral.of(name);
  }
}