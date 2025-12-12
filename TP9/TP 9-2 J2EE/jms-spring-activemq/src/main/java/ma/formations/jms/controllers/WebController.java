package ma.formations.jms.controllers;

import lombok.AllArgsConstructor;
import ma.formations.jms.model.Employee;
import ma.formations.jms.service.JmsServiceReceiver;
import ma.formations.jms.service.JmsServiceSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring6.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class WebController {

    private JmsServiceSender sender;
    private JmsServiceReceiver receiver;

    @GetMapping
    public String welcome(Model m) {
        m.addAttribute("emp", new Employee());
        return "form";
    }

    @PostMapping("/send")
    public String sendMessage(Employee emp, Model m) {
        sender.sendMessage("test-queue", emp);
        m.addAttribute("confirmation", "Message envoyé avec succès !");
        m.addAttribute("emp", new Employee());
        return "form";
    }

    @GetMapping("/messages")
    public String test(Model m) {
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(receiver.getMessages(), 1);
        m.addAttribute("employees", reactiveDataDrivenMode);
        return "data";
    }
}
