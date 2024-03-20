package com.valentinpopescu98.storemanagement.user.authorities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Authority {

    @Id
    @SequenceGenerator(allocationSize = 1, name = "authority_seq", sequenceName = "authority_seq")
    @GeneratedValue(generator = "authority_seq", strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Role role;

    public Authority(Role role) {
        this.role = role;
    }

}
