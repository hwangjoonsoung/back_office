package org.cric.back_office.entity.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cric.back_office.entity.common.EditorEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Organization extends EditorEntity {

    @Id
    @Column(name = "organization_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String code;
    private String phone;
    private String address;
    private String businessRegistrationNumber ;
    private boolean active = false;

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<User>();
}
