package synonyms.infrastructure.wordreference

import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ListenableFutureTask
import synonyms.domain.CategorizedSynonyms
import synonyms.domain.SynonymsSource
import synonyms.domain.Term
import synonyms.infrastructure.jsoup.JsoupDocumentFetcher

import java.util.concurrent.Callable

class WordReferenceSynonymsSource implements SynonymsSource {

    private final JsoupDocumentFetcher documentFetcher
    private final WordReferenceSynonymsParser synonymsParser

    WordReferenceSynonymsSource(JsoupDocumentFetcher documentFetcher, WordReferenceSynonymsParser synonymsParser) {
        this.documentFetcher = documentFetcher
        this.synonymsParser = synonymsParser
    }

    @Override
    ListenableFuture<CategorizedSynonyms> synonymsFor(Term term) {
        def future = ListenableFutureTask.create(new Callable() {
            @Override
            CategorizedSynonyms call() throws Exception {
                def document = documentFetcher.fetch("http://wordreference.com/thesaurus/$term.value")
                return synonymsParser.categorizedSynonymsFromDocument(term, document)
            }
        })
        future.run()
        return future
    }
}
