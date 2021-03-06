package synonyms.infrastructure.wordreference

import com.intellij.util.concurrency.SameThreadExecutor
import org.jsoup.nodes.Document
import spock.lang.Specification
import synonyms.domain.CategorizedSynonyms
import synonyms.domain.Term
import synonyms.infrastructure.jsoup.JsoupDocumentFetcher

class WordReferenceSynonymsSourceSpec extends Specification {

    def documentFetcher = Stub(JsoupDocumentFetcher)
    def synonymsParser = Stub(WordReferenceSynonymsParser)
    def taskExecutor = new SameThreadExecutor()
    def synonymsSource = new WordReferenceSynonymsSource(documentFetcher, synonymsParser, taskExecutor)

    def "should return future with synonyms parsed from document from wordreference"() {
        given:
        def document = new Document("fake")
        def subject = new Term("house")
        def parsedSynonyms = new CategorizedSynonyms(subject, [], [:])

        documentFetcher.fetch("https://wordreference.com/synonyms/house") >> document
        synonymsParser.categorizedSynonymsFromDocument(subject, document) >> parsedSynonyms

        expect:
        synonymsSource.synonymsFor(subject).get() == parsedSynonyms
    }

}
