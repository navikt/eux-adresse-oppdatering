package no.nav.eux.adresse.oppdatering.integration.client.pdl.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class OpprettPdlKontaktadresse(
    val personopplysninger: List<Personopplysning>
) {

    data class Personopplysning(
        val endringsmelding: Endringsmelding,
        val ident: String,
        val endringstype: String = "OPPRETT",
        val opplysningstype: String = "KONTAKTADRESSE"
    )

    data class Endringsmelding(
        val kilde: String,
        @JsonProperty("@type")
        val type: String = "KONTAKTADRESSE",
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
        val gyldigFraOgMed: LocalDate = LocalDate.now(),
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
        val gyldigTilOgMed: LocalDate = LocalDate.now().plusYears(5),
        val adresse: Adresse
    )

    data class Adresse(
        val adressenavnNummer: String?,
        val bygningEtasjeLeilighet: String?,
        val postboksNummerNavn: String? = null,
        val postkode: String,
        val regionDistriktOmraade: String?,
        val landkode: String,
        @JsonProperty("@type")
        val type: String = "UTENLANDSK_ADRESSE"
    )
}
