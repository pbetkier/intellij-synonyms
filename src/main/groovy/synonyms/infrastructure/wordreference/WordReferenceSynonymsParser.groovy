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

        def divs =  article.select('.engthes > div')
        switch (divs.size()) {
            case 0:
                break
            case 1:
                def synonyms = extractSynonyms(divs.first())
                if (!synonyms.isEmpty()) {
                    def identity = new Sense(subject.value)
                    senses.add(identity)
                    synonymsBySenses.put(identity, synonyms)
                }
                break
            default:
                divs.each {
                    def sense = new Sense(it.select('b').text() - "Sense: ")
                    senses.add(sense)
                    synonymsBySenses.put(sense, extractSynonyms(it))
                }
        }

        return new CategorizedSynonyms(subject, senses, synonymsBySenses)
    }

    private List<Term> extractSynonyms(Element context) {
        return context.select('span').not('.tags')*.text().collect { new Term(it) }
    }

}
