package mediador;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorreDeControleTest {

    private TorreDeControle torre;
    private Aeronave a1;
    private Aeronave a2;
    private Aeronave a3;

    @BeforeEach
    void montarCenario() {
        torre = new TorreDeControle();
        a1 = new Aeronave("A1", torre);
        a2 = new Aeronave("A2", torre);
        a3 = new Aeronave("A3", torre);
    }

    @Test
    void pistaLivreAutorizaPouso() {
        a1.solicitarPouso();

        assertTrue(a1.getMensagens().get(0).contains("pouso autorizado"));
        assertSame(a1, torre.getOcupante());
    }

    @Test
    void pistaOcupadaMandaAeronaveParaFila() {
        a1.solicitarPouso();
        a2.solicitarDecolagem();

        assertTrue(a2.getMensagens().get(0).contains("pista ocupada por A1"));
        assertEquals(1, torre.getTamanhoFila());
        assertSame(a1, torre.getOcupante());
    }

    @Test
    void liberarPistaAutorizaOProximoDaFila() {
        a1.solicitarPouso();
        a2.solicitarDecolagem();

        a1.liberarPista();

        assertSame(a2, torre.getOcupante());
        assertTrue(a2.getMensagens().get(1).contains("decolagem autorizado"));
        assertEquals(0, torre.getTamanhoFila());
    }

    @Test
    void filaRespeitaOrdemDeChegada() {
        a1.solicitarPouso();
        a2.solicitarPouso();
        a3.solicitarDecolagem();

        a1.liberarPista();
        assertSame(a2, torre.getOcupante());

        a2.liberarPista();
        assertSame(a3, torre.getOcupante());

        a3.liberarPista();
        assertTrue(torre.isPistaLivre());
    }

    @Test
    void liberarPistaSemEstarNelaLancaExcecao() {
        a1.solicitarPouso();
        assertThrows(IllegalStateException.class, () -> a2.liberarPista());
    }
}