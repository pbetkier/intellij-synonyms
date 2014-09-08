package synonyms.infrastructure.task

import com.intellij.openapi.application.ApplicationManager

class ApplicationThreadPoolTaskExecutor implements TaskExecutor {

    @Override
    void execute(Runnable toExecute) {
        ApplicationManager.application.executeOnPooledThread(toExecute)
    }

}
