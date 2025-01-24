package no.nav.eux.adresse.oppdatering.integration.client.pdl.model


import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PdlVegadresseKtTest {

    @Test
    fun husnummer() {
        toHusnummerOrNull("Karl Johans gate 22") shouldBe "22"
        toHusnummerOrNull("Storgata 5") shouldBe "5"
        toHusnummerOrNull("Torggata 16B") shouldBe "16"
        toHusnummerOrNull("Parkveien 12A") shouldBe "12"
        toHusnummerOrNull("  Karl Johans gate   22  ") shouldBe "22"
        toHusnummerOrNull("Storgata    5") shouldBe "5"
        toHusnummerOrNull("Karl Johans gate") shouldBe null
        toHusnummerOrNull("Storgata") shouldBe null
        toHusnummerOrNull(null) shouldBe null
        toHusnummerOrNull("") shouldBe null
        toHusnummerOrNull(" ") shouldBe null
        toHusnummerOrNull("Strandveien gate B") shouldBe null
        toHusnummerOrNull("Langgata hus") shouldBe null
        toHusnummerOrNull("Gamleveien 12B") shouldBe "12"
        toHusnummerOrNull("Barkveien 78C") shouldBe "78"
        toHusnummerOrNull("Fjordgata 8C") shouldBe "8"
        toHusnummerOrNull("Hovedveien 44D") shouldBe "44"
        toHusnummerOrNull("Sørlandsveien 55 A") shouldBe "55"
        toHusnummerOrNull("Strandveien 45 B") shouldBe "45"
        toHusnummerOrNull("Korsfjellveien nr. 24") shouldBe "24"
    }

    @Test
    fun husbokstav() {
        toHusbokstavOrNull("Karl Johans gate 22A") shouldBe "A"
        toHusbokstavOrNull("Storgata 5B") shouldBe "B"
        toHusbokstavOrNull("Torggata 16B") shouldBe "B"
        toHusbokstavOrNull("Parkveien 12A") shouldBe "A"
        toHusbokstavOrNull("  Karl Johans gate   22A  ") shouldBe "A"
        toHusbokstavOrNull("Storgata    5B") shouldBe "B"
        toHusbokstavOrNull("Karl Johans gate 22") shouldBe null
        toHusbokstavOrNull("Storgata 5") shouldBe null
        toHusbokstavOrNull(null) shouldBe null
        toHusbokstavOrNull("") shouldBe null
        toHusbokstavOrNull(" ") shouldBe null
        toHusbokstavOrNull("Strandveien gate B") shouldBe null
        toHusbokstavOrNull("Langgata hus") shouldBe null
        toHusbokstavOrNull("Gamleveien 12B") shouldBe "B"
        toHusbokstavOrNull("Barkveien 78C") shouldBe "C"
        toHusbokstavOrNull("Fjordgata 8C") shouldBe "C"
        toHusbokstavOrNull("Hovedveien 44D") shouldBe "D"
        toHusbokstavOrNull("Sørlandsveien 55 A") shouldBe "A"
        toHusbokstavOrNull("Strandveien 45 B") shouldBe "B"
        toHusbokstavOrNull("Korsfjellveien nr. 24") shouldBe null
    }

    @Test
    fun adressenavn() {
        toAdressenavnOrNull("Karl Johans gate 22A") shouldBe "Karl Johans gate"
        toAdressenavnOrNull("Storgata 5B") shouldBe "Storgata"
        toAdressenavnOrNull("Torggata 16B") shouldBe "Torggata"
        toAdressenavnOrNull("Parkveien 12A") shouldBe "Parkveien"
        toAdressenavnOrNull("  Karl Johans gate   22A  ") shouldBe "Karl Johans gate"
        toAdressenavnOrNull("Storgata    5B") shouldBe "Storgata"
        toAdressenavnOrNull("Karl Johans gate") shouldBe "Karl Johans gate"
        toAdressenavnOrNull("Storgata") shouldBe "Storgata"
        toAdressenavnOrNull(null) shouldBe null
        toAdressenavnOrNull("") shouldBe null
        toAdressenavnOrNull(" ") shouldBe null
    }

    @Test
    fun tilleggsnavnOrNull() {
        toTilleggsnavnOrNull("H0101") shouldBe "H"
        toTilleggsnavnOrNull("H0101A") shouldBe "HA"
        toTilleggsnavnOrNull(null) shouldBe null
        toTilleggsnavnOrNull("") shouldBe null
        toTilleggsnavnOrNull(" ") shouldBe null
        toTilleggsnavnOrNull("0101") shouldBe null
        toTilleggsnavnOrNull("202") shouldBe null
        toTilleggsnavnOrNull("  H0101  ") shouldBe "H"
        toTilleggsnavnOrNull(" B0202  ") shouldBe "B"
    }
}
