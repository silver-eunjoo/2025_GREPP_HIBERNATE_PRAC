package io.silver.domain.eg2._3;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.TableGenerator;

@Entity
@TableGenerator(
        name="ACCOUNT_SEQ_TABLE",
        table="SEQUENCE_CHECK",
        pkColumnName = "OTHER_ACCOUNT_SEQ",
        initialValue = 1
)
public class OtherAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator="ACCOUNT_SEQ_TABLE")
    private Long id;

    @Column(length=50)
    private String name;

}
