package synonyms.application

import com.google.common.base.Optional
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import synonyms.domain.Term

class TermExtractor {

    Optional<Term> fromElement(PsiElement verified) {
        if (verified instanceof PsiIdentifier) {
            return Optional.of(new Term(verified.text))
        }
        return Optional.absent()
    }

}
