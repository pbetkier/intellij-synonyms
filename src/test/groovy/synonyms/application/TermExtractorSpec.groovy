package synonyms.application

import com.intellij.psi.PsiElement
import spock.lang.Specification
import spock.lang.Unroll
import synonyms.domain.Term

class TermExtractorSpec extends Specification {

    def extractor = new TermExtractor()

    def "should not extract term for element which doesn't contain any text"() {
        expect:
        !extractor.fromElement(Stub(PsiElement), 0).isPresent()
    }

    def "should extract term from a simple element with text"() {
        given:
        def simpleElement = Stub(PsiElement) {
            getText() >> "term"
        }

        when:
        def extracted = extractor.fromElement(simpleElement, 0)

        then:
        extracted.isPresent()
        extracted.get() == new Term("term")
    }

    @Unroll
    def "should extract term #expected from complex element with #text at #position"() {
        given:
        def complexElement = Stub(PsiElement) {
            getText() >> text
        }

        when:
        def extracted = extractor.fromElement(complexElement, position)

        then:
        extracted.isPresent()
        extracted.get() == new Term(expected)

        where:
        text                 | position                          || expected
        "wordInsideSentence" | "wordInsideSentence".indexOf('I') || "inside"
        "wordInsideSentence" | "wordInsideSentence".indexOf('s') || "inside"
        "firstWord"          | "firstWord".indexOf('f')          || "first"
        "lastWord"           | "lastWord".indexOf('d')           || "word"
        "with_underscore"    | "with_underscore".indexOf('n')    || "underscore"
        "withNumber10"       | "withNumber10".indexOf('e')       || "number"
        "wiThShoRtWords"     | "wiThShoRtWords".indexOf('d')     || "words"
    }

    def "doesn't extract terms shorter than 4 characters"() {
        given:
        def shortElement = Stub(PsiElement) {
            getText() >> "top"
        }

        when:
        def extracted = extractor.fromElement(shortElement, 0)

        then:
        !extracted.isPresent()
    }

}
