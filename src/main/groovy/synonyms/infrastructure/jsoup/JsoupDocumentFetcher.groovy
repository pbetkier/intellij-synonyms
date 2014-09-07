package synonyms.infrastructure.jsoup

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class JsoupDocumentFetcher {

    Document fetch(String url) {
        Jsoup.connect(url).get()
    }

}
