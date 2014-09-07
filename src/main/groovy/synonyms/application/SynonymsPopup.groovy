package synonyms.application

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.ui.popup.JBPopupFactory
import synonyms.domain.CategorizedSynonyms
import synonyms.domain.Term

import javax.swing.*

class SynonymsPopup {

    private final JBPopup popup

    private final JEditorPane synonymsPane

    SynonymsPopup(Term term, Project project) {
        synonymsPane = new JEditorPane("text/html", "Fetching synonyms...")

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

    void populateWith(CategorizedSynonyms synonyms) {
        synonymsPane.text = synonyms.senses().collect {
            "<p><h3>$it.value</h3>${synonyms.synonymsOfSense(it)*.value.join(', ')}</p>"
        }.join("")

        resizeToFitContent()
    }

    void populateWithError(String message) {
        synonymsPane.text = "Problem with fetching synonyms, reason: $message."
        resizeToFitContent()
    }

    private void resizeToFitContent() {
        popup.pack(true, true)
    }
}
