package synonyms.application

import com.intellij.util.ui.UIUtil
import synonyms.domain.CategorizedSynonyms

import javax.swing.*
import javax.swing.text.html.HTMLDocument
import java.awt.*

class SynonymsPane extends JEditorPane {

    SynonymsPane() {
        super("text/html", "")
        (document as HTMLDocument).styleSheet.addRule(cssForFont(UIUtil.listFont))
        editable = false
    }

    void populateWithSynonyms(CategorizedSynonyms synonyms) {
        if (synonyms.senses().isEmpty()) {
            text = "No synonyms found for $synonyms.subject.value."
        } else {
            text = synonyms.senses().collect {
                "<div><h4>$it.value</h4>${synonyms.synonymsOfSense(it)*.value.join(', ')}</div>"
            }.join("")
        }
    }

    private String cssForFont(Font font) {
        "body { font-family: $font.family; font-size: $font.size pt; }"
    }
}
