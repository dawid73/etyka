package pl.com.fenice.etyka.answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.fenice.etyka.notification.Notification;
import pl.com.fenice.etyka.notification.NotificationRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    private final NotificationRepository notificationRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository, NotificationRepository notificationRepository) {
        this.answerRepository = answerRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<Answer> showAnswerByIdNotification(Notification notification){
        return answerRepository.findAnswerByNotification(notification);
    }

    @Transactional
    public void createAnswer(@NotNull Answer answer, String notificationNumer) throws EntityNotFoundException{
        Optional<Notification> notification = notificationRepository.findNotificationByNumer(notificationNumer);
        if(!notification.isPresent()){
            throw new EntityNotFoundException("Nie ma takiego zgloszenia o takim numerze");
        }
        answer.setNotification(notification.get());
        answerRepository.save(answer);
    }
}
