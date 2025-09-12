package practicum;


import org.junit.Test;
import ru.practicum.Bun;

import static org.junit.Assert.assertTrue;

public class BunNaNTest {
    @Test
    public void testPriceIsNaN() {
        float nanPrice = Float.NaN; // Уникальное значение NaN для проверки
        Bun bun = new Bun("TestBun", nanPrice); // Создаем объект с NaN ценой
        assertTrue("Цена булочки должна быть NaN", Float.isNaN(bun.getPrice()));
    }
}
