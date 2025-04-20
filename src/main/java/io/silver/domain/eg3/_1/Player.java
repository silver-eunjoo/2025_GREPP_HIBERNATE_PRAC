package io.silver.domain.eg3._1;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="players")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player {

    @Id
    @Column(name="player_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=50, nullable=false)
    private String name;

    @ManyToOne(fetch= FetchType.LAZY) // 쿼리를 보낼 때 한 번에 조인하지 않고, 프록시를 가져옴.
//    @ManyToOne
    @JoinColumn(name="team_id")
    private Team team;

}
