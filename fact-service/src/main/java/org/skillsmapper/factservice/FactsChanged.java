package org.skillsmapper.factservice;

import java.time.OffsetDateTime;
import java.util.List;

public record FactsChanged(String user, List<Fact> facts, OffsetDateTime timestamp) {

  @Override
  public String toString() {
    return "FactsChanged{" +
            "user='" + user + '\'' +
            ", facts=" + facts +
            ", timestamp=" + timestamp +
            '}';
  }

}
