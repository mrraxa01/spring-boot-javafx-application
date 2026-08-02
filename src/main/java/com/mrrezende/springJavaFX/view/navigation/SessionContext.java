package com.mrrezende.springJavaFX.view.navigation;

import org.springframework.stereotype.Component;

/**
 * Guarda estado simples que precisa ser compartilhado entre telas (ex: qual usuario
 * esta "ativo" no momento). Como o FxWeaver nao passa parametros diretamente ao
 * carregar uma view, controllers escrevem aqui antes de navegar, e o controller da
 * tela seguinte le esse valor no seu metodo @FXML initialize().
 * <p/>
 * Para fluxos mais complexos (varios dados, telas com parametros diferentes por
 * chamada), prefira um objeto de estado dedicado por fluxo em vez de crescer esta
 * classe genericamente.
 */
@Component
public class SessionContext {

    private Long currentUserId;

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }
}
