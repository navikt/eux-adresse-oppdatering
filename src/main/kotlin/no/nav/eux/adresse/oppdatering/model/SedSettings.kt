package no.nav.eux.adresse.oppdatering.model

fun harDirekteSpesifisertBostedsadresse(sedType: String) =
    when (sedType) {
        "S048", "S055" -> true
        else -> false
    }
