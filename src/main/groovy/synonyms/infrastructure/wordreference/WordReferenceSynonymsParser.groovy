package synonyms.infrastructure.wordreference

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import synonyms.domain.CategorizedSynonyms
import synonyms.domain.Sense
import synonyms.domain.Term

class WordReferenceSynonymsParser {

    CategorizedSynonyms categorizedSynonymsFromDocument(Term subject, Document toParse) {
        def senses = []
        def synonymsBySenses = [:]

        def article = toParse.getElementById('article')
        if (article == null) {
            throw new WordReferenceDocumentParsingException(subject, "missing 'article' element")
        }

        if (article.select('.liSense')) {
            article.select('.liSense').each {
                def sense = new Sense(it.select('.synsense').text() - "Sense: ")
                senses.add(sense)
                synonymsBySenses.put(sense, extractSynonyms(it))
            }
        } else {
            def synonyms = extractSynonyms(article)
            if (synonyms) {
                def identity = new Sense(subject.value)
                senses.add(identity)
                synonymsBySenses.put(identity, synonyms)
            }
        }

        return new CategorizedSynonyms(subject, senses, synonymsBySenses)
    }

    private List<Term> extractSynonyms(Element context) {
        return context.select('.synonym')*.text().collect { new Term(it) }
    }

}
