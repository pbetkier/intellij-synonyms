package synonyms.application

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.ui.popup.JBPopupFactory
import synonyms.domain.Synonyms
import synonyms.domain.Term

import javax.swing.*

class SynonymsPopup {

    private final JBPopup popup

    private final JEditorPane synonymsPane

    SynonymsPopup(Term term, Project project) {
        synonymsPane = new JEditorPane("text/html", "Loading...")

        popup = JBPopupFactory.getInstance().createComponentPopupBuilder(synonymsPane, synonymsPane)
                .setProject(project)
                .setResizable(true)
                .setMovable(true)
                .setTitle("Synonyms for ${term.value}")
                .setRequestFocus(true)
                .createPopup();
    }

    void show(DataContext dataContext) {
        popup.showInBestPositionFor(dataContext)
    }

    void populateWith(Synonyms synonyms) {
        synonymsPane.text = synonyms.toString()
    }
}
