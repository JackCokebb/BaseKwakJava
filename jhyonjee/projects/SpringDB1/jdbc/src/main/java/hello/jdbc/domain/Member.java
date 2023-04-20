package hello.jdbc.domain;

import lombok.Data;

@Data //toString이 포함되어 있음
public class Member {
    private String memberId;
    private int money;

    public Member() {

    }

    public Member(String memberId, int money) {
        this.memberId = memberId;
        this.money = money;
    }
}
