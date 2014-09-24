package synonyms.infrastructure.wordreference

import synonyms.domain.Term

class WordReferenceDocumentParsingException extends RuntimeException {

    WordReferenceDocumentParsingException(Term subject, String reason) {
        super("Couldn't parse wordreference document for $subject.value because of $reason.")
    }
}
