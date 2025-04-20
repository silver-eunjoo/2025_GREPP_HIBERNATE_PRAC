package io.silver.domain.eg3._2;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Locker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer number;

//    @OneToOne(mappedBy = "locker")
//    private LockerOwner owner;
    // 사실 이건 양방향일 때 붙여주면 됨. 즉, 락커에서 주인을 참조해야할 경우가 있을 때!
    // 근데 보통... 회원 -> 락커 몇 번? 이거기 때문에 단방향도 가능
    // 이 락커 -> 누구 거? -> 양방향해야 함!
}
