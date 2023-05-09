package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient;
import tacos.Taco;
import tacos.TacoOrder;
import tacos.data.IngredientRepository;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j // will automatically generate an SLF4J Logger static property
@Controller // mark it as a candidate for component scanning
@RequestMapping("/design") // specifies this will handle requests whose path begins with /design
@SessionAttributes("tacoOrder") // TacoOrder object that is put into the model should be maintained in session
public class DesignTacoController {
    private final IngredientRepository ingredientRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        Iterable<Ingredient> ingredients = ingredientRepo.findAll();

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
    public String processTaco(
        @Valid Taco taco,
        Errors errors,
        @ModelAttribute TacoOrder tacoOrder
    ) {
        if (errors.hasErrors()) {
            log.info("Error processing taco: {}", errors);
            return "design";
        }

        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);

        return "redirect:/orders/current";
    }


    private Iterable<Ingredient> filterByType(
        Iterable<Ingredient> ingredients, Ingredient.Type type) {
        return StreamSupport
            .stream(ingredients.spliterator(), false)
            .filter(x -> x.getType().equals(type))
            .collect(Collectors.toList());
    }
}
