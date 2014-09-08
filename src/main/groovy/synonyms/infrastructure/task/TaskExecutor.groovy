package synonyms.infrastructure.task

interface TaskExecutor {

    void execute(Runnable toExecute)

}