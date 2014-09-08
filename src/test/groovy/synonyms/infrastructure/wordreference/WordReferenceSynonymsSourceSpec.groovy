package synonyms.infrastructure.wordreference

import org.jsoup.nodes.Document
import spock.lang.Specification
import synonyms.domain.CategorizedSynonyms
import synonyms.domain.Term
import synonyms.infrastructure.jsoup.JsoupDocumentFetcher
import synonyms.infrastructure.task.InCurrentThreadTaskExecutor

class WordReferenceSynonymsSourceSpec extends Specification {

    def documentFetcher = Stub(JsoupDocumentFetcher)
    def synonymsParser = Stub(WordReferenceSynonymsParser)
    def taskExecutor = new InCurrentThreadTaskExecutor()
    def synonymsSource = new WordReferenceSynonymsSource(documentFetcher, synonymsParser, taskExecutor)

    def "should return future with synonyms parsed from document from wordreference"() {
        given:
        def document = new Document("fake")
        def subject = new Term("house")
        def parsedSynonyms = new CategorizedSynonyms(subject, [], [:])

        documentFetcher.fetch("http://wordreference.com/thesaurus/house") >> document
        synonymsParser.categorizedSynonymsFromDocument(subject, document) >> parsedSynonyms

        expect:
        synonymsSource.synonymsFor(subject).get() == parsedSynonyms
    }

}
