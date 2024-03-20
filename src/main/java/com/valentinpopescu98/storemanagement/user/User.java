package com.valentinpopescu98.storemanagement.user;

import com.valentinpopescu98.storemanagement.user.authorities.Authority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @SequenceGenerator(allocationSize = 1, name = "user_seq", sequenceName = "user_seq")
    @GeneratedValue(generator = "user_seq", strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private boolean isLocked = false;
    private boolean isEnabled = true;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "authority_id", referencedColumnName = "id")
    private Authority authority;

    public User(String username, String password, boolean isLocked, boolean isEnabled, Authority authority) {
        this.username = username;
        this.password = password;
        this.isLocked = isLocked;
        this.isEnabled = isEnabled;
        this.authority = authority;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authority.getRole().getGrantedAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}
