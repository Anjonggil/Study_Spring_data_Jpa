package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;

    @Test
    void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertEquals(findMember1,member1);
        assertEquals(findMember2,member2);

//        List<Member> findMembers = memberJpaRepository.findAll();
//        assertEquals(findMembers.size(),2);
//
//        long count = memberJpaRepository.count();
//        assertEquals(count,2);
//
//        memberJpaRepository.delete(member1);
//        memberJpaRepository.delete(member2);
//
//        long deletedCount = memberJpaRepository.count();
//        assertEquals(deletedCount,0);

        findMember1.setUsername("member change!");
    }

    @Test
    public void findByUsernameAngAgeGreaterThen(){
        Member m1 = new Member("m1", 10);
        Member m2 = new Member("m1", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> findMembers = memberRepository.findByUsernameAndAgeGreaterThan("m1", 15);

        for (Member findMember : findMembers) {
            assertEquals(findMember.getUsername(),m2.getUsername());
            assertEquals(findMember.getAge(),m2.getAge());
        }

    }

    @Test
    void TestNameQuery(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMembers = memberRepository.findByUsername("member1");
        assertEquals(findMembers.get(0),member1);
    }

    @Test
    void TestQuery(){
        Member member1 = new Member("member1",20);
        Member member2 = new Member("member2",10);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMembers = memberRepository.findUser("member1",20);
        assertEquals(findMembers.get(0),member1);
    }

}