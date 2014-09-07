package synonyms.infrastructure.wordreference

import synonyms.infrastructure.jsoup.JsoupDocumentFetcher

class WordReferenceSynonymsSourceFactory {

    static WordReferenceSynonymsSource create() {
        return new WordReferenceSynonymsSource(new JsoupDocumentFetcher(), new WordReferenceSynonymsParser())
    }

}
