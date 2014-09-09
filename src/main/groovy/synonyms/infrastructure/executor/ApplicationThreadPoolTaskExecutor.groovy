package synonyms.infrastructure.executor

import com.intellij.openapi.application.ApplicationManager

import java.util.concurrent.Executor

class ApplicationThreadPoolTaskExecutor implements Executor {

    @Override
    void execute(Runnable command) {
        ApplicationManager.application.executeOnPooledThread(command)
    }

}
