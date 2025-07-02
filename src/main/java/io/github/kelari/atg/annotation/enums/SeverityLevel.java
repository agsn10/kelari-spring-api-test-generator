package io.github.kelari.atg.annotation.enums;

public enum SeverityLevel {
    MEDIUM,
    BLOCKER,   // quebra geral do sistema, impede uso completo
    CRITICAL,  // erro grave, fluxo principal interrompido
    MAJOR,     // erro significativo, mas contornável
    MINOR,     // falha leve, impacto pequeno
    TRIVIAL    // não afeta funcionalidade (ex: estética)
}
