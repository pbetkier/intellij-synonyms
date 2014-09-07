package synonyms.domain

import com.google.common.base.Preconditions
import com.google.common.collect.ImmutableList

class CategorizedSynonyms {

    private final List<Sense> senses
    private final Map<Sense, List<Term>> synonymsBySense
    final Term subject

    CategorizedSynonyms(Term subject, List<Sense> senses, Map<Sense, List<Term>> synonymsBySense) {
        Preconditions.checkArgument(synonymsBySense.keySet() == senses as Set,
                "keys in synonymsBySense map must match provided senses list!" as Object)
        this.subject = subject
        this.senses = senses
        this.synonymsBySense = synonymsBySense
    }

    List<Sense> senses() {
        return ImmutableList.copyOf(senses)
    }

    List<Term> synonymsOfSense(Sense sense) {
        return ImmutableList.copyOf(synonymsBySense[sense])
    }

}
