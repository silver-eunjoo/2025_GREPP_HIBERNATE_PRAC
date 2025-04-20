package io.silver.domain.eg2._3;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@SequenceGenerator(
        name="ACCOUNT_SEQ_GENERATOR",
        sequenceName = "ACCOUNT_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class Account {

    @Id
    @Column(name="account_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_SEQ_GENERATOR")
    private Long id;

    private String name;
}
