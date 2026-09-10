package flbas1.pipboy.controller;

import flbas1.pipboy.model.MenuProfile;
import flbas1.pipboy.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final MenuService menuService;

    public ProfileController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public List<MenuProfile> getProfiles() {
        return menuService.getProfiles();
    }

    @GetMapping("/{profileName}")
    public MenuProfile getProfile(
            @PathVariable String profileName) {
        return menuService.getProfile(profileName);
    }
}