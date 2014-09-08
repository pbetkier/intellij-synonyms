package synonyms.infrastructure.wordreference

import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ListenableFutureTask
import synonyms.domain.CategorizedSynonyms
import synonyms.domain.SynonymsSource
import synonyms.domain.Term
import synonyms.infrastructure.jsoup.JsoupDocumentFetcher
import synonyms.infrastructure.task.TaskExecutor

import java.util.concurrent.Callable

class WordReferenceSynonymsSource implements SynonymsSource {

    private final JsoupDocumentFetcher documentFetcher
    private final WordReferenceSynonymsParser synonymsParser
    private final TaskExecutor executor

    WordReferenceSynonymsSource(JsoupDocumentFetcher documentFetcher,
                                WordReferenceSynonymsParser synonymsParser,
                                TaskExecutor executor) {
        this.documentFetcher = documentFetcher
        this.synonymsParser = synonymsParser
        this.executor = executor
    }

    @Override
    ListenableFuture<CategorizedSynonyms> synonymsFor(Term term) {
        def findSynonymsTask = ListenableFutureTask.create(new Callable() {
            @Override
            CategorizedSynonyms call() throws Exception {
                def document = documentFetcher.fetch("http://wordreference.com/thesaurus/$term.value")
                return synonymsParser.categorizedSynonymsFromDocument(term, document)
            }
        })

        executor.execute(findSynonymsTask)
        return findSynonymsTask
    }
}
