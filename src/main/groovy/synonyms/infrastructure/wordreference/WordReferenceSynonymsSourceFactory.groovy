package synonyms.infrastructure.wordreference

import synonyms.infrastructure.jsoup.JsoupDocumentFetcher
import synonyms.infrastructure.executor.ApplicationThreadPoolTaskExecutor

class WordReferenceSynonymsSourceFactory {

    static WordReferenceSynonymsSource create() {
        return new WordReferenceSynonymsSource(new JsoupDocumentFetcher(),
                new WordReferenceSynonymsParser(), new ApplicationThreadPoolTaskExecutor())
    }

}
