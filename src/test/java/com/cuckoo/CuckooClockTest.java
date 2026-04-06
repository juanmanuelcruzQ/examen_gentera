package com.cuckoo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("CuckooClock - Extra tests para cobertura")
class CuckooClockTest {

    private CuckooClock clock;

    @BeforeEach
    void setUp() {
        clock = new CuckooClock();
    }

    @Nested
    @DisplayName("Validación adicional")
    class ValidacionExtra {

        @ParameterizedTest(name = "entrada blank [{0}] debe rechazarse")
        @ValueSource(strings = { " ", "   ", "\t", "\n" })
        @DisplayName("strings en blanco lanzan excepción")
        void dadoBlank_deberiaLanzarExcepcion(String entrada) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> clock.tick(entrada))
                    .withMessageContaining("nula o vacía");
        }
    }
}