package synonyms.application

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
    void actionPerformed(AnActionEvent e) {
        Editor editor = CommonDataKeys.EDITOR.getData(e.dataContext)
        PsiElement element = PsiUtilBase.getElementAtCaret(editor)

        Optional<Term> extracted = termExtractor.fromElement(element, editor.caretModel.offset - element.textOffset)
        if (!extracted.present) {
            return
        }

        def popup = new SynonymsPopup(extracted.get(), e.project)
        popup.show(e.dataContext)

        ListenableFuture<CategorizedSynonyms> synonymsFuture = synonymsSource.synonymsFor(extracted.get())
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
