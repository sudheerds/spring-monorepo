package com.example.notification.controller;

import com.example.notification.entity.Notification;
import com.example.notification.repository.NotificationRepository;
import com.example.observability.annotation.Track;
import com.example.validation.annotation.Password;
import com.example.validation.annotation.PhoneNumber;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@Validated
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/getNotifications")
    @PreAuthorize("hasRole('USER')")
    @Track("getNotifications")
    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }

    @PostMapping("/sendNotification")
    @PreAuthorize("hasRole('ADMIN')")
    @Track("sendNotification")
    public Notification sendNotification(@RequestParam String recipient,
                                         @RequestParam String message) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setMessage(message);
        return notificationRepository.save(notification);
    }

    @PostMapping("/validateContact")
    @PreAuthorize("hasRole('USER')")
    @Track("validateContact")
    public String validateContact(
            @RequestParam @PhoneNumber(message = "Invalid phone number format") String phone,
            @RequestParam @Password(message = "Password must meet complexity requirements") String password) {
        return "Phone and password are valid!";
    }
}
