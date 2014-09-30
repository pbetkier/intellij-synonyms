package synonyms.application.ui

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.ui.popup.JBPopupFactory
import synonyms.domain.CategorizedSynonyms
import synonyms.domain.Term

class SynonymsPopup {

    private final JBPopup popup
    private final SynonymsPane synonymsPane

    SynonymsPopup(Term term, Project project) {
        synonymsPane = new SynonymsPane()
        synonymsPane.text = "Fetching synonyms..."

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

    void populateWithSynonyms(CategorizedSynonyms synonyms) {
        synonymsPane.populateWithSynonyms(synonyms)
        resizeToFitContent()
    }

    void populateWithError(String message) {
        synonymsPane.populateWithError(message)
        resizeToFitContent()
    }

    private void resizeToFitContent() {
        synonymsPane.resizeToFitContent()
        popup.pack(true, true)
    }
}
