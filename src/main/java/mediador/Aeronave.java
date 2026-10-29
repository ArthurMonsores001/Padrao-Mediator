package mediador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aeronave {

    private final String identificador;
    private final IMediador mediador;
    private final List<String> mensagens = new ArrayList<>();

    public Aeronave(String identificador, IMediador mediador) {
        if (identificador == null || identificador.trim().isEmpty()) {            throw new IllegalArgumentException("O identificador da aeronave e obrigatorio");
        }
        if (mediador == null) {
            throw new IllegalArgumentException("O mediador e obrigatorio");
        }
        this.identificador = identificador;
        this.mediador = mediador;
        this.mediador.registrar(this);
    }

    public void solicitarPouso() {
        mediador.solicitarPouso(this);
    }

    public void solicitarDecolagem() {
        mediador.solicitarDecolagem(this);
    }

    public void liberarPista() {
        mediador.liberarPista(this);
    }

    public void receber(String mensagem) {
        mensagens.add(mensagem);
    }

    public String getIdentificador() {
        return identificador;
    }

    public List<String> getMensagens() {
        return Collections.unmodifiableList(mensagens);
    }
}