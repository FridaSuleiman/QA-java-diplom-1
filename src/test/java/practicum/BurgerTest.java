package practicum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import ru.practicum.Burger;
import ru.practicum.Bun;
import ru.practicum.Ingredient;
import ru.practicum.IngredientType;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class BurgerTest {

    private Burger burger;
    private Bun bun;
    private Ingredient ingredient;
    private Ingredient ingredient2;

    // Параметры для тестов
    private final float bunPrice;
    private final String bunName;
    private final IngredientType ingredientType;
    private final String ingredientName;
    private final float ingredientPrice;
    private final IngredientType ingredientType2;
    private final String ingredientName2;
    private final float ingredientPrice2;

    public BurgerTest(float bunPrice, String bunName, IngredientType ingredientType, String ingredientName,
                      float ingredientPrice, IngredientType ingredientType2, String ingredientName2, float ingredientPrice2) {
        this.bunPrice = bunPrice;
        this.bunName = bunName;
        this.ingredientType = ingredientType;
        this.ingredientName = ingredientName;
        this.ingredientPrice = ingredientPrice;
        this.ingredientType2 = ingredientType2;
        this.ingredientName2 = ingredientName2;
        this.ingredientPrice2 = ingredientPrice2;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {100.0f, "white bun", IngredientType.SAUCE, "hot sauce", 50.0f, IngredientType.FILLING, "cutlet", 100.0f},
                {200.0f, "dark bun", IngredientType.FILLING, "cheese", 80.0f, IngredientType.SAUCE, "mustard", 30.0f},
                {150.0f, "red bun", null, null, 0.0f, null, null, 0.0f} // Тест без ингредиентов
        });
    }

    @Before
    public void setUp() {
        burger = new Burger();
        bun = Mockito.mock(Bun.class);

        Mockito.when(bun.getPrice()).thenReturn(bunPrice);
        Mockito.when(bun.getName()).thenReturn(bunName);

        if (ingredientType != null) {
            ingredient = Mockito.mock(Ingredient.class);
            Mockito.when(ingredient.getPrice()).thenReturn(ingredientPrice);
            Mockito.when(ingredient.getName()).thenReturn(ingredientName);
            Mockito.when(ingredient.getType()).thenReturn(ingredientType);
        }

        if (ingredientType2 != null) {
            ingredient2 = Mockito.mock(Ingredient.class);
            Mockito.when(ingredient2.getPrice()).thenReturn(ingredientPrice2);
            Mockito.when(ingredient2.getName()).thenReturn(ingredientName2);
            Mockito.when(ingredient2.getType()).thenReturn(ingredientType2);
        }
    }

    @Test
    public void testSetBuns() {
        burger.setBuns(bun);
        assertEquals("Булочка должна быть установлена", bun, burger.bun);
    }

    @Test
    public void testAddIngredient() {
        if (ingredient != null) {
            burger.addIngredient(ingredient);
            assertEquals("Ингредиент должен быть добавлен", 1, burger.ingredients.size());
        }
    }

    @Test
    public void testRemoveIngredient() {
        if (ingredient != null) {
            burger.addIngredient(ingredient);
            burger.removeIngredient(0);
            assertEquals("Ингредиент должен быть удален", 0, burger.ingredients.size());
        }
    }

    @Test
    public void testMoveIngredient() {
        if (ingredient != null && ingredient2 != null) {
            burger.addIngredient(ingredient);
            burger.addIngredient(ingredient2);
            burger.moveIngredient(1, 0);
            assertEquals("Ингредиент должен быть перемещен", ingredient2, burger.ingredients.get(0));
            assertEquals("Исходный ингредиент должен быть сдвинут", ingredient, burger.ingredients.get(1));
        }
    }

    @Test
    public void testGetReceiptFormat() {
        burger.setBuns(bun);

        if (ingredient != null) {
            burger.addIngredient(ingredient);
        }

        String actualReceipt = burger.getReceipt();
        float expectedPrice = bun.getPrice() * 2 + (ingredient != null ? ingredient.getPrice() : 0);

        StringBuilder expectedReceipt = new StringBuilder();
        expectedReceipt.append(String.format("(==== %s ====)%n", bun.getName()));

        if (ingredient != null) {
            expectedReceipt.append(String.format("= %s %s =%n",
                    ingredient.getType().name().toLowerCase(),
                    ingredient.getName()));
        }

        expectedReceipt.append(String.format("(==== %s ====)%n", bun.getName()));
        expectedReceipt.append(String.format("%nPrice: %.6f%n", expectedPrice));

        assertThat("Формат рецепта должен соответствовать",
                actualReceipt, equalTo(expectedReceipt.toString()));
    }

    @Test
    public void testAddMultipleIngredientsPrice() {
        burger.setBuns(bun);

        if (ingredient != null) {
            burger.addIngredient(ingredient);
        }
        if (ingredient2 != null) {
            burger.addIngredient(ingredient2);
        }

        float expectedPrice = bun.getPrice() * 2 +
                (ingredient != null ? ingredient.getPrice() : 0) +
                (ingredient2 != null ? ingredient2.getPrice() : 0);

        assertThat("Цена с несколькими ингредиентами должна быть корректной",
                burger.getPrice(), equalTo(expectedPrice));
    }
}