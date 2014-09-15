package synonyms.application

import com.google.common.base.Optional
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.spellchecker.inspections.IdentifierSplitter
import synonyms.domain.Term

class TermExtractor {

    Optional<Term> fromElement(PsiElement verified, int cursorPosition) {
        if (verified instanceof PsiIdentifier) {
            List<TextRange> ranges = []
            new IdentifierSplitter().split(verified.text, new TextRange(0, verified.text.size()), {
                ranges.add(it)
            })

            int currentPosition = 0
            for (TextRange range in ranges) {
                currentPosition += range.length
                if (currentPosition > cursorPosition) {
                    return Optional.of(new Term(range.substring(verified.text).toLowerCase()))
                }
            }
        }
        return Optional.absent()
    }

}
