package com.polus.fibicomp.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SCIENCE_KEYWORD")
public class ScienceKeyword implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "SCIENCE_KEYWORD_CODE", updatable = false, nullable = false)
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String scienceCode) {
        this.code = scienceCode;
    }
}
