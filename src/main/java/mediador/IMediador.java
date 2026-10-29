package mediador;

public interface IMediador {

    void registrar(Aeronave aeronave);

    void solicitarPouso(Aeronave aeronave);

    void solicitarDecolagem(Aeronave aeronave);

    void liberarPista(Aeronave aeronave);
}
