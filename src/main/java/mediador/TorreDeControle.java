package mediador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TorreDeControle implements IMediador {

    private final List<Aeronave> aeronaves = new ArrayList<>();
    private final Queue<PedidoPista> fila = new LinkedList<>();
    private Aeronave ocupante;

    @Override
    public void registrar(Aeronave aeronave) {
        if (!aeronaves.contains(aeronave)) {
            aeronaves.add(aeronave);
        }
    }

    @Override
    public void solicitarPouso(Aeronave aeronave) {
        solicitar(aeronave, "pouso");
    }

    @Override
    public void solicitarDecolagem(Aeronave aeronave) {
        solicitar(aeronave, "decolagem");
    }

    @Override
    public void liberarPista(Aeronave aeronave) {
        validarRegistro(aeronave);
        if (ocupante != aeronave) {
            throw new IllegalStateException("A aeronave " + aeronave.getIdentificador() + " nao esta usando a pista");
        }

        ocupante = null;
        aeronave.receber("Torre: pista liberada, obrigado");

        PedidoPista proximo = fila.poll();
        if (proximo != null) {
            ocupante = proximo.getAeronave();
            ocupante.receber("Torre: pista livre, " + proximo.getOperacao() + " autorizado");
            avisarPosicoesNaFila();
        }
    }

    public boolean isPistaLivre() {
        return ocupante == null;
    }

    public Aeronave getOcupante() {
        return ocupante;
    }

    public int getTamanhoFila() {
        return fila.size();
    }

    private void solicitar(Aeronave aeronave, String operacao) {
        validarRegistro(aeronave);
        if (aeronave == ocupante || estaNaFila(aeronave)) {
            throw new IllegalStateException("A aeronave " + aeronave.getIdentificador() + " ja possui um pedido em andamento");
        }

        if (ocupante == null) {
            ocupante = aeronave;
            aeronave.receber("Torre: " + operacao + " autorizado");
        } else {
            fila.add(new PedidoPista(aeronave, operacao));
            aeronave.receber("Torre: pista ocupada por " + ocupante.getIdentificador()
                    + ", aguarde. Posicao na fila: " + fila.size());
        }
    }

    private void avisarPosicoesNaFila() {
        int posicao = 1;
        for (PedidoPista pedido : fila) {
            pedido.getAeronave().receber("Torre: sua posicao na fila agora e " + posicao);
            posicao++;
        }
    }

    private boolean estaNaFila(Aeronave aeronave) {
        for (PedidoPista pedido : fila) {
            if (pedido.getAeronave() == aeronave) {
                return true;
            }
        }
        return false;
    }

    private void validarRegistro(Aeronave aeronave) {
        if (!aeronaves.contains(aeronave)) {
            throw new IllegalArgumentException("Aeronave nao registrada na torre");
        }
    }

    private static class PedidoPista {

        private final Aeronave aeronave;
        private final String operacao;

        PedidoPista(Aeronave aeronave, String operacao) {
            this.aeronave = aeronave;
            this.operacao = operacao;
        }

        Aeronave getAeronave() {
            return aeronave;
        }

        String getOperacao() {
            return operacao;
        }
    }
}