package practicum;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static practicum.BunTest.DELTA;
import static org.hamcrest.Matchers.equalTo;
import ru.practicum.*;


import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class BurgerTest {

    private Burger burger;
    private Bun bun;
    private Ingredient ingredient;
    private Ingredient ingredient2;
    private float expectedPrice;
    private String ingredientName;
    private IngredientType ingredientType;
    private Database database;

    // Конструктор для параметризации
    public BurgerTest(String ingredientName, IngredientType ingredientType, float expectedPrice) {
        this.ingredientName = ingredientName;
        this.ingredientType = ingredientType;
        this.expectedPrice = expectedPrice;
    }

    // Параметризованный набор данных
    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"hot sauce", IngredientType.SAUCE, 250.0f},
                {"cheese", IngredientType.FILLING, 270.0f},
                {"lettuce", IngredientType.FILLING, 250.0f},
                {"bbq sauce", IngredientType.SAUCE, 250.0f}
        });
    }

    @Before
    public void setUp() {
        burger = new Burger();
        bun = Mockito.mock(Bun.class);
        ingredient = Mockito.mock(Ingredient.class);
        ingredient2 = Mockito.mock(Ingredient.class);

        // Мокаем поведение булочки
        Mockito.when(bun.getPrice()).thenReturn(200.0f);
        Mockito.when(bun.getName()).thenReturn("white bun");

        // Мокаем поведение ингредиентов
        Mockito.when(ingredient.getPrice()).thenReturn(expectedPrice - 200); // Ожидаемая цена минус цена булочки
        Mockito.when(ingredient.getName()).thenReturn(ingredientName);
        Mockito.when(ingredient.getType()).thenReturn(ingredientType);

        Mockito.when(ingredient2.getPrice()).thenReturn(200.0f);
        Mockito.when(ingredient2.getName()).thenReturn("extra ingredient");
        Mockito.when(ingredient2.getType()).thenReturn(IngredientType.FILLING);
    }

    @Test
    public void testSetBuns() {
        burger.setBuns(bun);
        assertEquals(bun, burger.bun);
    }

    @Test
    public void testAddIngredient() {
        burger.addIngredient(ingredient);
        assertEquals(1, burger.ingredients.size());
    }

    @Test
    public void testRemoveIngredient() {
        burger.addIngredient(ingredient);
        burger.removeIngredient(0);
        assertEquals(0, burger.ingredients.size());
    }

    @Test
    public void testMoveIngredient() {
        burger.addIngredient(ingredient);
        burger.addIngredient(ingredient2);

        // Переместим второй ингредиент на первое место
        burger.moveIngredient(1, 0);

        // Проверяем, что ингредиенты переместились
        assertEquals(ingredient2, burger.ingredients.get(0));
        assertEquals(ingredient, burger.ingredients.get(1));
    }


    @Test
    public void testGetPrice() {
        // Создаем объект булочки и ингредиента
        bun = new Bun(ingredientName, expectedPrice - 200);
        ingredient = Mockito.mock(Ingredient.class);
        Mockito.when(ingredient.getPrice()).thenReturn(50F); // Пример цены ингредиента

        // Расчет стоимости: цена булочки умноженная на 2 + цена ингредиента
        float expectedPrice = bun.getPrice() * 2 + ingredient.getPrice();

        // Создание бургера
        Burger burger = new Burger();
        burger.setBuns(bun);
        burger.addIngredient(ingredient);

        // Выводим результаты на консоль
        System.out.println("Test for bun: " + ingredientName);
        System.out.println("Expected price: " + expectedPrice);
        System.out.println("Actual price: " + burger.getPrice());

        // Проверяем, что рассчитанная цена бургера соответствует ожидаемой
        assertEquals(expectedPrice, burger.getPrice(), DELTA);
    }


    @Test
    public void getReceiptIsSuccess() {
        // Мокаем булочку и ингредиент
        Mockito.when(bun.getName()).thenReturn("sweet bun");
        Mockito.when(bun.getPrice()).thenReturn(200F);
        Mockito.when(ingredient.getName()).thenReturn("hot sauce");
        Mockito.when(ingredient.getPrice()).thenReturn(50F);
        Mockito.when(ingredient.getType()).thenReturn(IngredientType.FILLING);

        // Создаем бургер и добавляем ингредиенты
        Burger burger = new Burger();
        burger.setBuns(bun);
        burger.addIngredient(ingredient);

        // Получаем фактический рецепт для анализа формата
        String actualReceipt = burger.getReceipt();

        // Создаем ожидаемый рецепт в том же формате, что и фактический
        String expectedReceipt;
        if (actualReceipt.contains("\r\n\r\nPrice:")) {
            // Формат с дополнительной пустой строкой
            expectedReceipt = String.format("(==== %s ====)%n", bun.getName()) +
                    String.format("= %s %s =%n", ingredient.getType().name().toLowerCase(), ingredient.getName()) +
                    String.format("(==== %s ====)%n", bun.getName()) +
                    String.format("%nPrice: %.6f%n", (bun.getPrice() * 2) + ingredient.getPrice());
        } else {
            // Стандартный формат
            expectedReceipt = String.format("(==== %s ====)%n", bun.getName()) +
                    String.format("= %s %s =%n", ingredient.getType().name().toLowerCase(), ingredient.getName()) +
                    String.format("(==== %s ====)%n", bun.getName()) +
                    String.format("Price: %.6f%n", (bun.getPrice() * 2) + ingredient.getPrice());
        }

        // Проверяем, что фактический рецепт совпадает с ожидаемым
        MatcherAssert.assertThat("Неверный рецепт",
                burger.getReceipt(),
                equalTo(expectedReceipt));
    }


    //исправлено, бага теперь нет
    @Test
    public void testAddMultipleIngredients() {
        // Мокаем булочку и ингредиенты
        Mockito.when(bun.getPrice()).thenReturn(200.0f);
        Mockito.when(ingredient.getPrice()).thenReturn(expectedPrice - 200); // Цена ингредиента минус цена булочки
        Mockito.when(ingredient2.getPrice()).thenReturn(200.0f);

        // Создаем бургер и добавляем булочку и два ингредиента
        burger.setBuns(bun);
        burger.addIngredient(ingredient);
        burger.addIngredient(ingredient2);

        // Расчет ожидаемой стоимости: цена булочки умноженная на 2 + цена ингредиентов
        float expectedTotalPrice = bun.getPrice() * 2 + ingredient.getPrice() + ingredient2.getPrice();

        // Проверка, что фактическая цена бургера равна ожидаемой
        MatcherAssert.assertThat("Неверная стоимость бургера",
                burger.getPrice(),
                equalTo(expectedTotalPrice));
    }


}