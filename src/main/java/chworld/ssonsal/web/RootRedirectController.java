package chworld.ssonsal.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class RootRedirectController {

    @GetMapping("/")
    public RedirectView redirectToHealth() {
        return new RedirectView("/health");
    } 
}