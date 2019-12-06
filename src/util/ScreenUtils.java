package util;

import java.awt.*;

public class ScreenUtils {

    /**
     * Calcula o tamanho máximo utilizável da tela.
     * @param config GraphicsConfiguration do Frame.
     * @return um Rectangle contendo as coordenadas máximas que a janela pode ter.
     */
    public static Rectangle getMaxWindowSize(GraphicsConfiguration config) {
        // Tamanho da tela
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Tamanho da barra de tarefas
        Insets screenMax = Toolkit.getDefaultToolkit().getScreenInsets(config);
        int tbHeight = screenMax.bottom;

        // Calcula partindo do x = 0 e y = 0, a largura é a mesma da tela,
        // e a altura é a altura calculada da tela menos a altura da barra de tarefas.
        return new Rectangle(0, 0, screenSize.width, screenSize.height - tbHeight);
    }
}
