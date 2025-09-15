package practicum;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.practicum.*;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class BurgerTest {

    private Burger burger;
    private Bun bun;
    private Ingredient ingredient;
    private Ingredient ingredient2;

    @Before
    public void setUp() {
        burger = new Burger();
        bun = Mockito.mock(Bun.class);
        ingredient = Mockito.mock(Ingredient.class);
        ingredient2 = Mockito.mock(Ingredient.class);

        Mockito.when(bun.getPrice()).thenReturn(200.0f);
        Mockito.when(bun.getName()).thenReturn("white bun");

        Mockito.when(ingredient.getPrice()).thenReturn(50.0f);
        Mockito.when(ingredient.getName()).thenReturn("hot sauce");
        Mockito.when(ingredient.getType()).thenReturn(IngredientType.SAUCE);

        Mockito.when(ingredient2.getPrice()).thenReturn(200.0f);
        Mockito.when(ingredient2.getName()).thenReturn("extra ingredient");
        Mockito.when(ingredient2.getType()).thenReturn(IngredientType.FILLING);
    }

    @Test
    public void testSetBuns() {
        burger.setBuns(bun);
        assertEquals("Булочка должна быть установлена", bun, burger.bun);
    }

    @Test
    public void testAddIngredient() {
        burger.addIngredient(ingredient);
        assertEquals("Ингредиент должен быть добавлен", 1, burger.ingredients.size());
    }

    @Test
    public void testRemoveIngredient() {
        burger.addIngredient(ingredient);
        burger.removeIngredient(0);
        assertEquals("Ингредиент должен быть удален", 0, burger.ingredients.size());
    }

    @Test
    public void testMoveIngredientNewPosition() {
        burger.addIngredient(ingredient);
        burger.addIngredient(ingredient2);
        burger.moveIngredient(1, 0);
        assertEquals("Ингредиент должен быть перемещен на новую позицию",
                ingredient2, burger.ingredients.get(0));
    }

    @Test
    public void testMoveIngredientOldPosition() {
        burger.addIngredient(ingredient);
        burger.addIngredient(ingredient2);
        burger.moveIngredient(1, 0);
        assertEquals("Исходный ингредиент должен быть сдвинут",
                ingredient, burger.ingredients.get(1));
    }

    @Test
    public void testGetReceiptFormat() {
        Mockito.when(bun.getName()).thenReturn("sweet bun");
        Mockito.when(ingredient.getName()).thenReturn("hot sauce");
        Mockito.when(ingredient.getType()).thenReturn(IngredientType.SAUCE);

        burger.setBuns(bun);
        burger.addIngredient(ingredient);

        String actualReceipt = burger.getReceipt();
        float expectedPrice = bun.getPrice() * 2 + ingredient.getPrice();

        String expectedReceipt = String.format("(==== %s ====)%n", bun.getName()) +
                String.format("= %s %s =%n", ingredient.getType().name().toLowerCase(), ingredient.getName()) +
                String.format("(==== %s ====)%n", bun.getName()) +
                String.format("%nPrice: %.6f%n", expectedPrice);

        MatcherAssert.assertThat("Формат рецепта должен соответствовать",
                actualReceipt, equalTo(expectedReceipt));
    }

    @Test
    public void testAddMultipleIngredientsPrice() {
        burger.setBuns(bun);
        burger.addIngredient(ingredient);
        burger.addIngredient(ingredient2);

        float expectedTotalPrice = bun.getPrice() * 2 + ingredient.getPrice() + ingredient2.getPrice();

        MatcherAssert.assertThat("Цена с несколькими ингредиентами должна быть корректной",
                burger.getPrice(), equalTo(expectedTotalPrice));
    }
}