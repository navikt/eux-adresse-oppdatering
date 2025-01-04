package no.nav.eux.adresse.oppdatering.integration.client.pdl.model

data class PdlEndringsstatus(
    val status: Status,
    val ident: String?,
    val endringstype: String,
    val opplysningstype: String?,
    val opprettet: String?,
    val innmeldtEndring: String?,
    val sendt: Boolean,
)

data class Status(
    val endringId: Long,
    val grunnlagId: Long,
    val statusType: String,
    val substatus: List<Substatus>?,
)

data class Substatus(
    val referanse: String?,
    val domene: String?,
)
