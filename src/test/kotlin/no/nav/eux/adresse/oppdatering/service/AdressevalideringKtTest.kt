package no.nav.eux.adresse.oppdatering.service

import io.kotest.matchers.shouldBe
import no.nav.eux.adresse.oppdatering.integration.client.eux.rina.api.model.EuxRinaApiDokument
import no.nav.eux.adresse.oppdatering.model.Adresse
import org.junit.jupiter.api.Test

class AdressetransformeringKtTest {

    @Test
    fun `validertAdresse should validate and transform address correctly`() {
        val adresse = EuxRinaApiDokument.Adresse(
            gate = "Karl Johans gate 22",
            bygning = "Bygning A",
            by = "Oslo",
            postnummer = "0159",
            region = "Oslo",
            landkode = "NO",
            type = "bosted"
        )

        val expectedAdresse = Adresse(
            adressenavnNummer = "Karl Johans gate 22",
            bygningEtasjeLeilighet = "Bygning A",
            bySted = "Oslo",
            postkode = "0159",
            regionDistriktOmraade = "Oslo",
            landkode = "NO"
        )

        adresse.transformertAdresse shouldBe expectedAdresse
    }

    @Test
    fun `transformertAdresse should handle null fields`() {
        val adresse = EuxRinaApiDokument.Adresse(
            gate = null,
            bygning = null,
            by = null,
            postnummer = null,
            region = null,
            landkode = "NO",
            type = "bosted"
        )

        val expectedAdresse = Adresse(
            adressenavnNummer = null,
            bygningEtasjeLeilighet = null,
            bySted = null,
            postkode = null,
            regionDistriktOmraade = null,
            landkode = "NO"
        )

        adresse.transformertAdresse shouldBe expectedAdresse
    }

    @Test
    fun `transformertAdresse should handle special characters`() {
        val adresse = EuxRinaApiDokument.Adresse(
            gate = "Rue de l'Église",
            bygning = "Bâtiment B",
            by = "Paris",
            postnummer = "75001",
            region = "Île-de-France",
            landkode = "FR",
            type = "bosted"
        )

        val expectedAdresse = Adresse(
            adressenavnNummer = "Rue de l'Église",
            bygningEtasjeLeilighet = "Bâtiment B",
            bySted = "Paris",
            postkode = "75001",
            regionDistriktOmraade = "Île-de-France",
            landkode = "FR"
        )

        adresse.transformertAdresse shouldBe expectedAdresse
    }

    @Test
    fun `transformertAdresse should handle empty strings`() {
        val adresse = EuxRinaApiDokument.Adresse(
            gate = "",
            bygning = "",
            by = "",
            postnummer = "",
            region = "",
            landkode = "NO",
            type = "bosted"
        )

        val expectedAdresse = Adresse(
            adressenavnNummer = null,
            bygningEtasjeLeilighet = null,
            bySted = null,
            postkode = null,
            regionDistriktOmraade = null,
            landkode = "NO"
        )

        adresse.transformertAdresse shouldBe expectedAdresse
    }

    @Test
    fun `transformertAdresse should handle mixed case`() {
        val adresse = EuxRinaApiDokument.Adresse(
            gate = "kArL jOhAnS gAtE 22",
            bygning = "bYgNiNg A",
            by = "Oslo",
            postnummer = "0159",
            region = "Oslo",
            landkode = "NO",
            type = "bosted"
        )

        val expectedAdresse = Adresse(
            adressenavnNummer = "kArL jOhAnS gAtE 22",
            bygningEtasjeLeilighet = "bYgNiNg A",
            bySted = "Oslo",
            postkode = "0159",
            regionDistriktOmraade = "Oslo",
            landkode = "NO",
        )

        adresse.transformertAdresse shouldBe expectedAdresse
    }

    @Test
    fun `transformertAdresse should move postbox address to postboksNummerNavn`() {
        val adresse = EuxRinaApiDokument.Adresse(
            gate = "Postboks 123",
            bygning = "Bygning B",
            by = "Stockholm",
            postnummer = "100 12",
            region = "Stockholm",
            landkode = "SE",
            type = "bosted"
        )

        val expectedAdresse = Adresse(
            adressenavnNummer = null,
            bygningEtasjeLeilighet = "Bygning B",
            bySted = "Stockholm",
            postkode = "100 12",
            regionDistriktOmraade = "Stockholm",
            landkode = "SE",
            postboksNummerNavn = "Postboks 123"
        )

        adresse.transformertAdresse shouldBe expectedAdresse
    }

    @Test
    fun `validertAdresse should handle unknown address fields`() {
        val adresse = EuxRinaApiDokument.Adresse(
            gate = "Ukjent",
            bygning = "Ukjent",
            by = "Ukjent",
            postnummer = "Ukjent",
            region = "Ukjent",
            landkode = "NO",
            type = "bosted"
        )

        val expectedAdresse = Adresse(
            adressenavnNummer = null,
            bygningEtasjeLeilighet = null,
            bySted = null,
            postkode = null,
            regionDistriktOmraade = null,
            landkode = "NO"
        )

        adresse.transformertAdresse shouldBe expectedAdresse
    }

    @Test
    fun `transformertAdresse should move bygning to gate`() {
        val adresse = EuxRinaApiDokument.Adresse(
            gate = "Karl Johans gate",
            bygning = "22",
            by = "Oslo",
            postnummer = "0159",
            region = "Oslo",
            landkode = "NO",
            type = "bosted"
        )

        val expectedAdresse = Adresse(
            adressenavnNummer = "Karl Johans gate 22",
            bygningEtasjeLeilighet = null,
            bySted = "Oslo",
            postkode = "0159",
            regionDistriktOmraade = "Oslo",
            landkode = "NO"
        )

        adresse.transformertAdresse shouldBe expectedAdresse
    }

    @Test
    fun `transformertAdresse should not move bygning to gate`() {
        val adresse = EuxRinaApiDokument.Adresse(
            gate = "Karl Johans gate",
            bygning = "22a",
            by = "Oslo",
            postnummer = "0159",
            region = "Oslo",
            landkode = "NO",
            type = "bosted"
        )

        val expectedAdresse = Adresse(
            adressenavnNummer = "Karl Johans gate",
            bygningEtasjeLeilighet = "22a",
            bySted = "Oslo",
            postkode = "0159",
            regionDistriktOmraade = "Oslo",
            landkode = "NO"
        )

        adresse.transformertAdresse shouldBe expectedAdresse
    }

    @Test
    fun `transformertAdresse should replace special character No with letters No`() {
        val adresse = EuxRinaApiDokument.Adresse(
            gate = "Karl Johans gate № 22",
            bygning = "Bygning A",
            by = "Oslo",
            postnummer = "0159",
            region = "Oslo",
            landkode = "NO",
            type = "bosted"
        )

        val expectedAdresse = Adresse(
            adressenavnNummer = "Karl Johans gate No 22",
            bygningEtasjeLeilighet = "Bygning A",
            bySted = "Oslo",
            postkode = "0159",
            regionDistriktOmraade = "Oslo",
            landkode = "NO"
        )

        adresse.transformertAdresse shouldBe expectedAdresse
    }
}
