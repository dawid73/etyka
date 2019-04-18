package pl.com.fenice.etyka.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.com.fenice.etyka.answer.Answer;
import pl.com.fenice.etyka.answer.AnswerService;

import javax.validation.Valid;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final AnswerService answerService;

    @Autowired
    public NotificationController(NotificationService notificationService, AnswerService answerService){
        this.notificationService = notificationService;
        this.answerService = answerService;
    }

    @GetMapping("/start")
    public String start(Model model){

        return "start";
    }

    @GetMapping("/search")
    public String open(Model model){
        model.addAttribute("notification", new Notification());
        return "openoneform";
    }

    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("notification", new Notification());
        return "addnewnotification";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addtoDB(@Valid Notification notification, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "addnewnotification";
        }else {

            String numerZgloszenia = randomString(8);

            notification.setNumer(numerZgloszenia);
            model.addAttribute("numerZgloszenia", numerZgloszenia);
            notificationService.saveOrUpdate(notification);
            return "created";
        }
    }

    @RequestMapping(value = "/addanswer/{notificationNumer}", method = RequestMethod.POST)
    public String addtoDBAnswer(@Valid Answer answer, BindingResult bindingResult, @PathVariable("notificationNumer") String notificationNumer, Model model){

        //TODO zabrać numer tego zgłoszenia które dodajemy odpowiedź i przekazać POST-em do onenotification, a  nie redirect do show/[numer]
        // albo zamienić numer zamiast ID


            Notification notification = new Notification();
            notification.setNumer(notificationNumer);

            answerService.createAnswer(answer, notificationNumer);
            model.addAttribute("notification", notification);
//todo przekazać postem ten obiekt notification do openone
            return "redirect:/notification/openone";
            //return new ModelAndView(notification/openone, model);
    }

    @GetMapping("/showall")
    public String showall(Model model){
        List<Notification> notificationList = notificationService.getAllNotifications();
        model.addAttribute("notificationList", notificationList);
        return "notification";
    }

    @RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
    public String showone(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes){
        Optional<Notification> oneNotification = notificationService.getOne(id);

        if(oneNotification.isPresent()){
            model.addAttribute("oneNotification", oneNotification.get());
            model.addAttribute("newanswer", new Answer());
            List<Answer> answer = answerService.showAnswerByIdNotification(oneNotification.get());
            if(!answer.isEmpty()){
                model.addAttribute("answer", answer);
            }
            return "onenotification";
        }else {
            redirectAttributes.addFlashAttribute("error", "Błąd!! Nie ma użytkownika z takim ID !!");
            return "redirect:/notification/";
        }

    }

    @RequestMapping(value = "/openone", method = RequestMethod.POST)
    public String openone(@ModelAttribute(value = "notification") Notification notificationSearch , Model model, RedirectAttributes redirectAttributes){
        Optional<Notification> oneNotification = notificationService.getOneByNumber(notificationSearch.getNumer());

        if(oneNotification.isPresent()){
            model.addAttribute("oneNotification", oneNotification.get());
            model.addAttribute("newanswer", new Answer());
            List<Answer> answer = answerService.showAnswerByIdNotification(oneNotification.get());
            if(!answer.isEmpty()){
                model.addAttribute("answer", answer);
            }
            return "onenotification";
        }else {
            redirectAttributes.addFlashAttribute("error", "Błąd!! Nie ma użytkownika z takim ID !!");
            return "redirect:/notification";
        }

    }



//    Losowanie numeru zgłoszenia
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

}
