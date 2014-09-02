package synonyms.application

import com.intellij.psi.PsiComment
import com.intellij.psi.impl.source.tree.java.PsiIdentifierImpl
import spock.lang.Specification
import synonyms.domain.Term

class TermExtractorSpec extends Specification {

    def extractor = new TermExtractor()

    def "should not extract term for element which is not an identifier"() {
        expect:
        !extractor.fromElement(Stub(PsiComment)).isPresent()
    }

    def "should extract term from a simple identifier"() {
        when:
        def extracted = extractor.fromElement(new PsiIdentifierImpl("term"))

        then:
        extracted.isPresent()
        extracted.get() == new Term("term")
    }

}
