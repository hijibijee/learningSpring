package tacos;

import lombok.Data;

/**
 * IngredientRef will be automatically mapped to Ingredient_Ref table, and it has no identity column.
 * So no data annotation is required here.
 */
@Data
public class IngredientRef {
    private final String ingredient;
}
