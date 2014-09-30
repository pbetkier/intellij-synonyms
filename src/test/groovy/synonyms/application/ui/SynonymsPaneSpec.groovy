package synonyms.application.ui

import spock.lang.Specification
import synonyms.domain.CategorizedSynonyms
import synonyms.domain.Sense
import synonyms.domain.Term

class SynonymsPaneSpec extends Specification {

    def synonymsPane = new SynonymsPane()

    def "should populate with provided synonyms"() {
        given:
        def firstSense = new Sense('first sense')
        def secondSense = new Sense('second sense')

        def synonyms = new CategorizedSynonyms(
                new Term('house'),
                [firstSense, secondSense],
                [(firstSense): [new Term('dwelling'), new Term('residence')],
                 (secondSense): [new Term('congress')]])

        when:
        synonymsPane.populateWithSynonyms(synonyms)

        then:
        synonymsPane.text.contains "first sense"
        synonymsPane.text.contains "dwelling, residence"
        synonymsPane.text.contains "second sense"
        synonymsPane.text.contains "congress"
    }

    def "should display message about no synonyms for empty synonyms"() {
        given:
        def emptySynonyms = new CategorizedSynonyms(new Term("house"), [], [:])

        when:
        synonymsPane.populateWithSynonyms(emptySynonyms)

        then:
        synonymsPane.text.contains "No synonyms found for house."
    }

}
