package synonyms.domain

import com.google.common.util.concurrent.ListenableFuture

public interface SynonymsSource {

    ListenableFuture<CategorizedSynonyms> synonymsFor(Term term)

}