package flbas1.pipboy.controller;

import flbas1.pipboy.model.MenuProfile;
import flbas1.pipboy.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/menu")
    public MenuProfile getMenu() {
        return menuService.getActiveProfile();
    }
}

