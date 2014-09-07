package synonyms.domain

import com.google.common.util.concurrent.ListenableFuture

public interface SynonymsSource {

    ListenableFuture<Synonyms> synonymsFor(Term term)

}