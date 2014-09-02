package synonyms.application

import com.google.common.base.Optional
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiUtilBase
import synonyms.domain.Term

class ShowSynonymsAction extends AnAction {

    private final TermExtractor termExtractor = new TermExtractor()

    @Override
    void actionPerformed(AnActionEvent e) {
        Editor editor = CommonDataKeys.EDITOR.getData(e.dataContext)
        PsiElement element = PsiUtilBase.getElementAtCaret(editor)

        Optional<Term> extracted = termExtractor.fromElement(element)

        if (extracted.present) {
            notify(extracted.get().value)
        }
    }

    private void notify(String text) {
        def notification = new Notification("", "", "got $text", NotificationType.INFORMATION)
        ApplicationManager.application.messageBus.syncPublisher(Notifications.TOPIC).notify(notification)
    }

}
