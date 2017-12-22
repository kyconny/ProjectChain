package ninja.kyle.projectchain;

import java.time.ZonedDateTime;

public class QueryRecorder {

  private final ZonedDateTime birthDay;

  private ZonedDateTime lastQuery;

  public QueryRecorder() {
    this.birthDay = ZonedDateTime.now();
  }

  public void trigger() {
    lastQuery = ZonedDateTime.now();
  }

  public ZonedDateTime getBirthDay() {
    return birthDay;
  }

  public ZonedDateTime getLastQuery() {
    return lastQuery;
  }
}
