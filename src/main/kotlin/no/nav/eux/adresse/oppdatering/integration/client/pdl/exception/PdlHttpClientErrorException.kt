package no.nav.eux.adresse.oppdatering.integration.client.pdl.exception

open class PdlHttpClientErrorException(cause: Throwable) : RuntimeException(cause)

class PdlApiUgyldigIdentException(cause: Throwable) : PdlHttpClientErrorException(cause)
class PdlApiPersonNotFoundException(cause: Throwable) : PdlHttpClientErrorException(cause)
class PdlMottakUnprocessableEntityException(cause: Throwable) : PdlHttpClientErrorException(cause)
class PdlMottakConflictException(cause: Throwable) : PdlHttpClientErrorException(cause)
class PdlNotFoundException(cause: Throwable) : PdlHttpClientErrorException(cause)
