package synonyms.application

import com.intellij.psi.PsiComment
import com.intellij.psi.impl.source.tree.java.PsiIdentifierImpl
import spock.lang.Specification
import spock.lang.Unroll
import synonyms.domain.Term

class TermExtractorSpec extends Specification {

    def extractor = new TermExtractor()

    def "should not extract term for element which is not an identifier"() {
        expect:
        !extractor.fromElement(Stub(PsiComment), 0).isPresent()
    }

    def "should extract term from a simple identifier"() {
        when:
        def extracted = extractor.fromElement(new PsiIdentifierImpl("term"), 0)

        then:
        extracted.isPresent()
        extracted.get() == new Term("term")
    }

    @Unroll
    def "should extract term #expected from complex identifier #identifier at #position"() {
        when:
        def extracted = extractor.fromElement(new PsiIdentifierImpl(identifier), position)

        then:
        extracted.isPresent()
        extracted.get() == new Term(expected)

        where:
        identifier           | position                          || expected
        "wordInsideSentence" | "wordInsideSentence".indexOf('I') || "inside"
        "wordInsideSentence" | "wordInsideSentence".indexOf('s') || "inside"
        "firstWord"          | "firstWord".indexOf('f')          || "first"
        "lastWord"           | "lastWord".indexOf('d')           || "word"
        "with_underscore"    | "with_underscore".indexOf('n')    || "underscore"
        "withNumber10"       | "withNumber10".indexOf('e')       || "number"
        "wiThShoRtWords"     | "wiThShoRtWords".indexOf('d')     || "words"
    }

    def "doesn't extract short terms"() {
        when:
        def extracted = extractor.fromElement(new PsiIdentifierImpl("top"), 0)

        then:
        !extracted.isPresent()
    }

}
