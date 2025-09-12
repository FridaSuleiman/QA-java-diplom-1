package practicum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.practicum.IngredientType;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

@RunWith(Parameterized.class)
public class IngredientTypeTest {

    private final IngredientType ingredientType;
    private final String ingredientTypeName;

    // Параметризованный конструктор
    public IngredientTypeTest(IngredientType ingredientType, String ingredientTypeName) {
        this.ingredientType = ingredientType;
        this.ingredientTypeName = ingredientTypeName;
    }

    // Данные для параметризации
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { IngredientType.SAUCE, "SAUCE" },
                { IngredientType.FILLING, "FILLING" }
        });
    }

    // Проверка метода valueOf()
    @Test
    public void testValueOf() {
        assertEquals(ingredientType, IngredientType.valueOf(ingredientTypeName));
    }

    // Проверка метода name()
    @Test
    public void testName() {
        assertEquals(ingredientTypeName, ingredientType.name());
    }

    // Проверка метода toString()
    @Test
    public void testToString() {
        assertEquals(ingredientTypeName, ingredientType.toString());
    }

    // Проверка, что выбрасывается исключение для несуществующего значения
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEnumValue() {
        IngredientType.valueOf("INVALID");
    }

    // Проверка равенства и неравенства элементов перечисления
    @Test
    public void testEqualityAndInequality() {
        assertEquals(IngredientType.SAUCE, IngredientType.SAUCE);
        assertEquals(IngredientType.FILLING, IngredientType.FILLING);
        assertNotEquals(IngredientType.SAUCE, IngredientType.FILLING);
    }

    // Проверка метода ordinal()
    @Test
    public void testOrdinal() {
        assertEquals(0, IngredientType.SAUCE.ordinal());
        assertEquals(1, IngredientType.FILLING.ordinal());
    }

    // НЕ ПАРАМЕТРИЗИРОВАННЫЙ ТЕСТ, ОСТАЕТСЯ В КЛАССЕ
    @Test
    public void testEnumValues() {
        // Получаем все значения перечисления
        IngredientType[] types = IngredientType.values();

        // Проверяем, что в перечислении два значения
        assertEquals(2, types.length); // Должно быть два значения

        // Проверяем, что перечисление содержит нужные элементы
        assertTrue(EnumSet.allOf(IngredientType.class).contains(IngredientType.SAUCE));
        assertTrue(EnumSet.allOf(IngredientType.class).contains(IngredientType.FILLING));
    }
}