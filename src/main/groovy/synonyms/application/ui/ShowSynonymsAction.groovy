package synonyms.application.ui

import com.google.common.base.Optional
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiUtilBase
import synonyms.application.TermExtractor
import synonyms.domain.CategorizedSynonyms
import synonyms.domain.SynonymsSource
import synonyms.domain.Term
import synonyms.infrastructure.executor.UiThreadExecutor
import synonyms.infrastructure.wordreference.WordReferenceSynonymsSourceFactory

class ShowSynonymsAction extends AnAction {

    private final TermExtractor termExtractor = new TermExtractor()
    private final SynonymsSource synonymsSource = WordReferenceSynonymsSourceFactory.create()
    private final UiThreadExecutor uiThreadExecutor = new UiThreadExecutor()

    @Override
    void actionPerformed(AnActionEvent event) {
        Editor editor = CommonDataKeys.EDITOR.getData(event.dataContext)
        PsiElement element = PsiUtilBase.getElementAtCaret(editor)

        Optional<Term> extracted = extractCurrentTerm(element, editor)
        if (!extracted.present) {
            return
        }

        SynonymsPopup popup = createSynonymsPopup(extracted.get(), event)
        showSynonymsForTermInPopup(popup, extracted.get())
    }

    private Optional<Term> extractCurrentTerm(PsiElement element, Editor editor) {
        return termExtractor.fromElement(element, editor.caretModel.offset - element.textOffset)
    }

    private SynonymsPopup createSynonymsPopup(Term extracted, AnActionEvent e) {
        def popup = new SynonymsPopup(extracted, e.project)
        popup.show(e.dataContext)
        return popup
    }

    private void showSynonymsForTermInPopup(SynonymsPopup popup, Term extracted) {
        popup.indicateFetchingInProgress()
        ListenableFuture<CategorizedSynonyms> synonymsFuture = synonymsSource.synonymsFor(extracted)
        Futures.addCallback(synonymsFuture, new PopulatePopupCallback(popup), uiThreadExecutor)
    }

    private static class PopulatePopupCallback implements FutureCallback<CategorizedSynonyms> {

        private final SynonymsPopup popup

        PopulatePopupCallback(SynonymsPopup popup) {
            this.popup = popup
        }

        @Override
        void onSuccess(CategorizedSynonyms synonyms) {
            popup.populateWithSynonyms(synonyms)
        }

        @Override
        void onFailure(Throwable cause) {
            popup.populateWithError("Obtaining synonyms failed with ${cause.class.simpleName}: $cause.message")
        }
    }

}
