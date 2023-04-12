package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient;
import tacos.Taco;
import tacos.TacoOrder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j // will automatically generate an SLF4J Logger static property
@Controller // mark it as a candidate for component scanning
@RequestMapping("/design") // specifies this will handle requests whose path begins with /design
@SessionAttributes("tacoOrder") // TacoOrder object that is put into the model should be maintained in session
public class DesignTacoController {
    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        List<Ingredient> ingredients = Arrays.asList(
            new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),
            new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),
            new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),
            new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN),
            new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES),
            new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES),
            new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE),
            new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE),
            new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE),
            new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE)
        );

        Ingredient.Type[] types = Ingredient.Type.values();
        for (Ingredient.Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                filterByType(ingredients, type));
        }
    }

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping // Along with RequestMapping defines GET request for /design
    public String showDesignForm() {
        return "design";
    }

    @PostMapping // maps to POST /design
    public String processTaco(Taco taco, @ModelAttribute TacoOrder tacoOrder) {
        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);

        return "redirect:/orders/current";
    }


    private Iterable<Ingredient> filterByType(
        List<Ingredient> ingredients, Ingredient.Type type) {
        return ingredients
            .stream()
            .filter(x -> x.getType().equals(type))
            .collect(Collectors.toList());
    }
}
