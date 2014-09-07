package synonyms.domain

class CategorizedSynonyms {

    private final Iterable<Sense> senses

    private final Map<Sense, Iterable<Term>> synonymsBySense

    CategorizedSynonyms(Iterable<Sense> senses, Map<Sense, Iterable<Term>> synonymsBySense) {
        this.senses = senses
        this.synonymsBySense = synonymsBySense
    }

    Iterable<Sense> senses() {
        return senses
    }

    Iterable<Term> synonymsOfSense(Sense sense) {
        return synonymsBySense[sense]
    }

}
