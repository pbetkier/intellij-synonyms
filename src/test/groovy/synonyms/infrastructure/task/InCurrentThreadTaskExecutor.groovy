package synonyms.infrastructure.task

class InCurrentThreadTaskExecutor implements TaskExecutor {

    @Override
    void execute(Runnable toExecute) {
        toExecute.run()
    }

}
