package synonyms.infrastructure

import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ListenableFutureTask
import synonyms.domain.CategorizedSynonyms
import synonyms.domain.Sense
import synonyms.domain.SynonymsSource
import synonyms.domain.Term

import java.util.concurrent.Callable

class FakeSynonymsSource implements SynonymsSource {

    @Override
    ListenableFuture<CategorizedSynonyms> synonymsFor(Term term) {
        def future = ListenableFutureTask.create(new Callable() {
            @Override
            Object call() throws Exception {
                def sense = new Sense("some sense")
                return new CategorizedSynonyms([sense], [(sense): [new Term("house"), new Term("grass")]])
            }
        })
        future.run()
        return future
    }

}
