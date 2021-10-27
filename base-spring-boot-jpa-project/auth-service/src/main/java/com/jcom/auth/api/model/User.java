package com.jcom.auth.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ms.core.common.model.EntityBase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "userinfo")
@ApiModel(description = "User data")
public class User extends EntityBase {

	@Column
	@ApiModelProperty(notes = "code")
    private String code;

	@Column
	@ApiModelProperty(notes = "email")
    private String email;
    
	@Column(name = "namecode")
	@ApiModelProperty(notes = "namecode")
	private String nameCode;

	@Column
    private String password;
    
	@Column
    @ApiModelProperty(notes = "first name")
    private String firstName;

	@Column
    @ApiModelProperty(notes = "last name")
    private String lastName;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] profileImageContainer;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] coverImageContainer;

//	@Column
//  private ImageContainer profileImageContainer = new ImageContainer();
//
//	@Column
//  private ImageContainer coverImageContainer = new ImageContainer();

	@Column
    private Date lastLoggedOn;

	@Column
    private Date registeredOn;

	@Column
    private Integer attempts;
    
	@Transient
    private EmailVerification emailVerification;

    @Column
    private Date lastPasswordResetDate;
        
	@Column(name = "langcode")
    @ApiModelProperty(notes = "User language")
    private String langCode;

	@ManyToMany(cascade = {
        CascadeType.MERGE
    })
    @JoinTable(name = "user_role",
        joinColumns = @JoinColumn(name = "id_user"),
        inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private List<Role> roles = new ArrayList<>();
    
    @ManyToMany(cascade = {
        CascadeType.MERGE
    })
    @JoinTable(name = "user_permission",
        joinColumns = @JoinColumn(name = "id_user"),
        inverseJoinColumns = @JoinColumn(name = "id_permission")
    )
    private List<Permission> permissions = new ArrayList<>();
    
    @Transient
    private SellerProfile sellerProfile = new SellerProfile();
    
    @Transient
    private BuyerProfile buyerProfile = new BuyerProfile();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
	public String getNameCode() {
		return nameCode;
	}

	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
	}

	@JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
	public byte[] getProfileImageContainer() {
		return profileImageContainer;
	}

	public void setProfileImageContainer(byte[] profileImageContainer) {
		this.profileImageContainer = profileImageContainer;
	}

	public byte[] getCoverImageContainer() {
		return coverImageContainer;
	}

	public void setCoverImageContainer(byte[] coverImageContainer) {
		this.coverImageContainer = coverImageContainer;
	}

	public Date getLastLoggedOn() {
        return lastLoggedOn;
    }

    public void setLastLoggedOn(Date lastLoggedOn) {
        this.lastLoggedOn = lastLoggedOn;
    }

    public Date getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(Date registeredOn) {
        this.registeredOn = registeredOn;
    }

    public Integer getAttempts() {
		return attempts;
	}

	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}

	@JsonIgnore
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	@JsonIgnore
	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	@JsonIgnore
	public EmailVerification getEmailVerification() {
		return emailVerification;
	}

	public void setEmailVerification(EmailVerification emailVerification) {
		this.emailVerification = emailVerification;
	}

	@JsonIgnore
	public Date getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	public void setLastPasswordResetDate(Date lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

	public SellerProfile getSellerProfile() {
		return sellerProfile;
	}

	public void setSellerProfile(SellerProfile sellerProfile) {
		this.sellerProfile = sellerProfile;
	}

	public BuyerProfile getBuyerProfile() {
		return buyerProfile;
	}

	public void setBuyerProfile(BuyerProfile buyerProfile) {
		this.buyerProfile = buyerProfile;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

}
