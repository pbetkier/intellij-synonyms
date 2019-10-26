package synonyms.infrastructure.wordreference

import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ListenableFutureTask
import synonyms.domain.CategorizedSynonyms
import synonyms.domain.SynonymsSource
import synonyms.domain.Term
import synonyms.infrastructure.jsoup.JsoupDocumentFetcher

import java.util.concurrent.Callable
import java.util.concurrent.Executor

class WordReferenceSynonymsSource implements SynonymsSource {

    private final JsoupDocumentFetcher documentFetcher
    private final WordReferenceSynonymsParser synonymsParser
    private final Executor fetchTaskExecutor

    WordReferenceSynonymsSource(JsoupDocumentFetcher documentFetcher,
                                WordReferenceSynonymsParser synonymsParser,
                                Executor fetchTaskExecutor) {
        this.documentFetcher = documentFetcher
        this.synonymsParser = synonymsParser
        this.fetchTaskExecutor = fetchTaskExecutor
    }

    @Override
    ListenableFuture<CategorizedSynonyms> synonymsFor(Term term) {
        def findSynonymsTask = ListenableFutureTask.create(new Callable() {
            @Override
            CategorizedSynonyms call() throws Exception {
                def document = documentFetcher.fetch("https://wordreference.com/synonyms/$term.value")
                return synonymsParser.categorizedSynonymsFromDocument(term, document)
            }
        })

        fetchTaskExecutor.execute(findSynonymsTask)
        return findSynonymsTask
    }
}
