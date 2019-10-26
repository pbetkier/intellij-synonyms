package synonyms.infrastructure.jsoup

import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class JsoupDocumentFetcher {

    private static final REDIRECTS_LIMIT = 5

    Document fetch(String url) {
        return Jsoup.connect(url).get()
        def redirects = 0
        def nextUrl = url
        def response = null
        while (redirects < REDIRECTS_LIMIT) {
            response = Jsoup.connect(nextUrl).followRedirects(true).execute()
            if (not3xx(response)) {
                break
            }
            redirects += 1
            nextUrl = response.header("location")
        }

        if (redirects == REDIRECTS_LIMIT) {
            throw new RuntimeException("Redirects limit of $REDIRECTS_LIMIT exceeded")
        }
        return response.parse()
    }

    private not3xx(Connection.Response response) {
        response.statusCode() / 100 != 3
    }
}
