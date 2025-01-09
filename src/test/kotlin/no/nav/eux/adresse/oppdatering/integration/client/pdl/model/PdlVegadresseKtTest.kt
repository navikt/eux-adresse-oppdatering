package no.nav.eux.adresse.oppdatering.integration.client.pdl.model


import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PdlVegadresseKtTest {

    @Test
    fun husnummer() {
        husnummerOrNull("Karl Johans gate 22") shouldBe "22"
        husnummerOrNull("Storgata 5") shouldBe "5"
        husnummerOrNull("Torggata 16B") shouldBe "16"
        husnummerOrNull("Parkveien 12A") shouldBe "12"
        husnummerOrNull("  Karl Johans gate   22  ") shouldBe "22"
        husnummerOrNull("Storgata    5") shouldBe "5"
        husnummerOrNull("Karl Johans gate") shouldBe null
        husnummerOrNull("Storgata") shouldBe null
        husnummerOrNull(null) shouldBe null
        husnummerOrNull("") shouldBe null
        husnummerOrNull(" ") shouldBe null
        husnummerOrNull("Strandveien gate B") shouldBe null
        husnummerOrNull("Langgata hus") shouldBe null
        husnummerOrNull("Gamleveien 12B") shouldBe "12"
        husnummerOrNull("Barkveien 78C") shouldBe "78"
        husnummerOrNull("Fjordgata 8C") shouldBe "8"
        husnummerOrNull("Hovedveien 44D") shouldBe "44"
        husnummerOrNull("Sørlandsveien 55 A") shouldBe "55"
        husnummerOrNull("Strandveien 45 B") shouldBe "45"
        husnummerOrNull("Korsfjellveien nr. 24") shouldBe "24"
    }

    @Test
    fun husbokstav() {
        husbokstavOrNull("Karl Johans gate 22A") shouldBe "A"
        husbokstavOrNull("Storgata 5B") shouldBe "B"
        husbokstavOrNull("Torggata 16B") shouldBe "B"
        husbokstavOrNull("Parkveien 12A") shouldBe "A"
        husbokstavOrNull("  Karl Johans gate   22A  ") shouldBe "A"
        husbokstavOrNull("Storgata    5B") shouldBe "B"
        husbokstavOrNull("Karl Johans gate 22") shouldBe null
        husbokstavOrNull("Storgata 5") shouldBe null
        husbokstavOrNull(null) shouldBe null
        husbokstavOrNull("") shouldBe null
        husbokstavOrNull(" ") shouldBe null
        husbokstavOrNull("Strandveien gate B") shouldBe null
        husbokstavOrNull("Langgata hus") shouldBe null
        husbokstavOrNull("Gamleveien 12B") shouldBe "B"
        husbokstavOrNull("Barkveien 78C") shouldBe "C"
        husbokstavOrNull("Fjordgata 8C") shouldBe "C"
        husbokstavOrNull("Hovedveien 44D") shouldBe "D"
        husbokstavOrNull("Sørlandsveien 55 A") shouldBe "A"
        husbokstavOrNull("Strandveien 45 B") shouldBe "B"
        husbokstavOrNull("Korsfjellveien nr. 24") shouldBe null
    }

    @Test
    fun adressenavn() {
        adressenavnOrNull("Karl Johans gate 22A") shouldBe "Karl Johans gate"
        adressenavnOrNull("Storgata 5B") shouldBe "Storgata"
        adressenavnOrNull("Torggata 16B") shouldBe "Torggata"
        adressenavnOrNull("Parkveien 12A") shouldBe "Parkveien"
        adressenavnOrNull("  Karl Johans gate   22A  ") shouldBe "Karl Johans gate"
        adressenavnOrNull("Storgata    5B") shouldBe "Storgata"
        adressenavnOrNull("Karl Johans gate") shouldBe "Karl Johans gate"
        adressenavnOrNull("Storgata") shouldBe "Storgata"
        adressenavnOrNull(null) shouldBe null
        adressenavnOrNull("") shouldBe null
        adressenavnOrNull(" ") shouldBe null
    }

    @Test
    fun tilleggsnavnOrNull() {
        tilleggsnavnOrNull("H0101") shouldBe "H"
        tilleggsnavnOrNull("H0101A") shouldBe "HA"
        tilleggsnavnOrNull(null) shouldBe null
        tilleggsnavnOrNull("") shouldBe null
        tilleggsnavnOrNull(" ") shouldBe null
        tilleggsnavnOrNull("0101") shouldBe null
        tilleggsnavnOrNull("202") shouldBe null
        tilleggsnavnOrNull("  H0101  ") shouldBe "H"
        tilleggsnavnOrNull(" B0202  ") shouldBe "B"
    }
}
