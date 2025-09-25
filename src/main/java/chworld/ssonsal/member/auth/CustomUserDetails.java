package chworld.ssonsal.member.auth;

import chworld.ssonsal.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final Member member;

    public Long getId() {
        return member.getId(); // Member 엔티티의 PK
    }

    public CustomUserDetails(Member member){
        this.member = member;
    }

    public String getName(){
        return member.getMemberName();
    }

    public String getEmail(){
        return member.getMemberEmail();
    }

    @Override
    public String getUsername(){
        return member.getMemberEmail();
    }

    @Override
    public String getPassword(){
        return member.getMemberPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_"+member.getRole().name()));
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
