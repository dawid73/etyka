package pl.com.fenice.etyka.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getAllNotifications(){
        return notificationRepository.findAll();
    }

    public void saveOrUpdate(Notification notification){
        notificationRepository.save(notification);
    }

    public Optional<Notification> getOne(int id) {
        return notificationRepository.findById(id);
    }

    public Optional<Notification> getOneByNumber(String numer) {return notificationRepository.findNotificationByNumer(numer);}
}
