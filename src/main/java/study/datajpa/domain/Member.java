package study.datajpa.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter @Setter
@ToString(of = {"id","username","age"})
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    protected Member() {
    }

    public Member(Long id, String username, int age, Team team) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.team = team;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null){
            this.changeTeam(team);
        }
    }

    public Member(String username) {
        this.username = username;
    }

    public void changeTeam(Team team){
        this.team = team;
        team.getMemberList().add(this);
    }
}
