package synonyms.infrastructure.wordreference

import com.google.common.base.Charsets
import com.google.common.io.Resources
import org.jsoup.nodes.Document
import spock.lang.Specification
import synonyms.domain.Sense
import synonyms.domain.Term

class WordReferenceSynonymsParserSpec extends Specification {

    static final int HOUSE_SENSES_COUNT = 4
    static final int HOUSE_FIRST_SENSE_SYNONYMS_COUNT = 3
    static final int YEAR_SYNONYMS_COUNT = 8

    def parser = new WordReferenceSynonymsParser()

    def "should parse synonyms from document with multiple senses"() {
        given:
        def documentWithMultipleSenses = readContentOf("wordreference/thesaurusPageForHouse.html")

        when:
        def result = parser.categorizedSynonymsFromDocument(new Term("house"), documentWithMultipleSenses)

        then:
        result.senses().size() == HOUSE_SENSES_COUNT
        result.synonymsOfSense(result.senses().first()).size() == HOUSE_FIRST_SENSE_SYNONYMS_COUNT
    }

    def "should parse synonyms from document without any senses with searched term as its only sense"() {
        given:
        def documentWithoutSenses = readContentOf("wordreference/thesaurusPageForYear.html")

        when:
        def result = parser.categorizedSynonymsFromDocument(new Term("year"), documentWithoutSenses)
        println result

        then:
        result.senses() == [new Sense("year")]
        result.synonymsOfSense(result.senses().first()).size() == YEAR_SYNONYMS_COUNT
    }

    def "should return empty result if provided document doesn't contain synonyms"() {
        given:
        def documentWithoutSynonyms = readContentOf("wordreference/thesaurusPageForNonexistingTerm.html")

        when:
        def result = parser.categorizedSynonymsFromDocument(new Term("nonexisting-term"), documentWithoutSynonyms)

        then:
        !result.senses()
    }

    def "should fail if provided document is empty"() {
        given:
        def emptyDocument = readContentOf("emptyPage.html")

        when:
        parser.categorizedSynonymsFromDocument(new Term("whatever"), emptyDocument)

        then:
        thrown WordReferenceDocumentParsingException
    }


    private Document readContentOf(String resourceName) {
        def content = Resources.toString(Resources.getResource(resourceName), Charsets.UTF_8)
        return new Document("fake").html(content) as Document
    }

}
