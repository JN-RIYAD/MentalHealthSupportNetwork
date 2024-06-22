package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "payment_history")
@NoArgsConstructor
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "payment_medium", columnDefinition = "nvarchar(20)", nullable = false)
    private String paymentMedium;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "sender_account", columnDefinition = "nvarchar(50)", nullable = false)
    private String senderAccount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "transaction_id", columnDefinition = "nvarchar(50)", nullable = false)
    private String transactionId;

    @Column(name = "reference", columnDefinition = "nvarchar(50)")
    private String reference;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "payment_date_and_time", nullable = false)
    private LocalDateTime paymentDateAndTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "request_date_and_time", nullable = false)
    private LocalDateTime requestDateAndTime;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    // Separate date and time getters
    public String getPaymentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return paymentDateAndTime.toLocalDate().format(formatter);
    }

    public LocalTime getPaymentTime() {
        return paymentDateAndTime.toLocalTime();
    }

    public String getRequestDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return requestDateAndTime.toLocalDate().format(formatter);
    }

    public LocalTime getRequestTime() {
        return requestDateAndTime.toLocalTime();
    }

    @Override
    public String toString() {
        return "PaymentHistory{" +
                "id=" + id +
                ", paymentMedium='" + paymentMedium + '\'' +
                ", amount='" + amount + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", reference=" + reference +
                ", paymentDateAndTime='" + paymentDateAndTime + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", user=" + user +
                '}';
    }
}
