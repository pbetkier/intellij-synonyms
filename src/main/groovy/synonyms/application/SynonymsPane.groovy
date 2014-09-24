package synonyms.application

import com.intellij.util.ui.UIUtil
import synonyms.domain.CategorizedSynonyms

import javax.swing.*
import javax.swing.text.html.HTMLDocument
import java.awt.*

class SynonymsPane extends JScrollPane {

    private static final int MAX_WIDTH = 600
    private static final int MAX_HEIGHT = 400

    private final JEditorPane editorPane

    SynonymsPane() {
        super(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
        border = null

        editorPane = new JEditorPane("text/html", "")
        (editorPane.document as HTMLDocument).styleSheet.addRule(cssForFont(UIUtil.listFont))
        editorPane.editable = false
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

    void populateWithError(String message) {
        def msg = "<h4>Problem with displaying synonyms, reason:</h4> $message"
        text = msg.endsWith('.') ? msg : "$msg."
    }

    void setText(String text) {
        editorPane.text = text
    }

    String getText() {
        return editorPane.text
    }

    void resizeToFitContent() {
        editorPane.size = new Dimension(
                width: Math.min(editorPane.preferredSize.width as int, MAX_WIDTH),
                height: Short.MAX_VALUE
        )
        editorPane.preferredSize = new Dimension(
                width: editorPane.size.width + verticalScrollBar.maximumSize.width as int,
                height: Math.min(editorPane.preferredSize.height as int, MAX_HEIGHT)
        )
        viewportView = editorPane
    }

    private String cssForFont(Font font) {
        "body { font-family: $font.family; font-size: $font.size pt; }"
    }
}
