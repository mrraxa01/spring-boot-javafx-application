package com.mrrezende.springJavaFX.config;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Resolve o diretorio correto de dados da aplicacao para cada sistema operacional,
 * seguindo as convencoes padrao de cada plataforma:
 * <p/>
 * - Windows: %LOCALAPPDATA%\{vendor}\{appName}
 * - macOS:   ~/Library/Application Support/{vendor}/{appName}
 * - Linux:   ~/.local/share/{vendor}/{appName}
 * <p/>
 * Esse diretorio NAO fica dentro da pasta de instalacao (Program Files), evitando
 * problemas de permissao (escrever em Program Files exige privilegio de administrador).
 */
public final class AppDataLocator {

    private AppDataLocator() {
    }

    public static Path resolveAppDataDir(String vendor, String appName) {
        String os = System.getProperty("os.name", "").toLowerCase();
        Path base;

        if (os.contains("win")) {
            String localAppData = System.getenv("LOCALAPPDATA");
            base = Path.of(localAppData != null ? localAppData : System.getProperty("user.home"));
        } else if (os.contains("mac")) {
            base = Path.of(System.getProperty("user.home"), "Library", "Application Support");
        } else {
            String xdgData = System.getenv("XDG_DATA_HOME");
            base = Path.of(xdgData != null ? xdgData : System.getProperty("user.home") + "/.local/share");
        }

        Path dir = base.resolve(vendor).resolve(appName);
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new UncheckedIOException("Nao foi possivel criar o diretorio de dados da aplicacao: " + dir, e);
        }
        return dir;
    }
}
