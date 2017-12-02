package ninja.kyle.projectchain.exchanges;

import java.util.concurrent.ExecutorService;

public class Submitter {

  private final ExecutorService executorService;

  Submitter(ExecutorService executorService) {
    this.executorService = executorService;
  }

  ExecutorService getExecutorService() {
    return executorService;
  }
}
