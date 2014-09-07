package synonyms.infrastructure

import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ListenableFutureTask
import synonyms.domain.Synonyms
import synonyms.domain.SynonymsSource
import synonyms.domain.Term

import java.util.concurrent.Callable

class FakeSynonymsSource implements SynonymsSource {

    @Override
    ListenableFuture<Synonyms> synonymsFor(Term term) {
        def future = ListenableFutureTask.create(new Callable() {
            @Override
            Object call() throws Exception {
                return new Synonyms()
            }
        })
        future.run()
        return future
    }

}
